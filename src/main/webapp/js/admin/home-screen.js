// Calendar elements
const calendarElement = document.getElementById('calendar');
const currentMonthElement = document.getElementById('current-month');
const previousMonthButton = document.getElementById('previous-month');
const nextMonthButton = document.getElementById('next-month');

const regexPassword = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&]).+$/;
const regexUsername = /^[a-zA-Z0-9]+\.[a-zA-Z0-9]+$/;
const regexEmail = /^[A-Za-z0-9._+-]+@[A-Za-z0-9-]+(\.[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)+$|^$/;


// Current date
let currentDate = new Date();

// Calendar notes
let calendarNotes = {};
let selectedDate = null;

// Popup elements
const noteOverlay = document.getElementById("noteOverlay");
const calendarNoteText = document.getElementById("calendarNoteText");
const saveNoteBtn = document.getElementById("saveNoteBtn");
const deleteNoteBtn = document.getElementById("deleteNoteBtn");
const confirmEventOverlay = document.getElementById("confirmEventOverlay");
const confirmEventCancel = document.getElementById("confirmEventCancel");
const confirmEventSend = document.getElementById("confirmEventSend");
const confirmEventTitle = document.getElementById("confirmEventTitle");

let eventAction = null;

// Tooltip
const tooltip = document.getElementById("calendarTooltip");

// Event elements
const eventForm = document.getElementById("eventForm");
const eventNameInput = document.getElementById("eventName");
const eventStartInput = document.getElementById("eventStart");
const eventEndInput = document.getElementById("eventEnd");
const eventPopupTitle = document.getElementById("eventPopupTitle");

// Days of the week
const daysOfWeek = ['DOM', 'SEG', 'TER', 'QUA', 'QUI', 'SEX', 'SAB'];

// Fixed Brazilian holidays
const fixedHolidays = {
    "01-01": {
        name: "Feriado - Confraternização Universal",
        description: "Primeiro dia do ano."
    },
    "04-21": {
        name: "Feriado - Tiradentes",
        description: "Homenagem a Joaquim José da Silva Xavier."
    },
    "05-01": {
        name: "Feriado - Dia do Trabalhador",
        description: "Celebração dos direitos dos trabalhadores."
    },
    "09-07": {
        name: "Feriado - Independência do Brasil",
        description: "Proclamação da independência em 1822."
    },
    "10-12": {
        name: "Feriado - Nossa Senhora Aparecida",
        description: "Padroeira do Brasil."
    },
    "11-02": {
        name: "Feriado - Finados",
        description: "Dia de homenagem aos falecidos."
    },
    "11-15": {
        name: "Feriado - Proclamação da República",
        description: "Fim do Império e início da República."
    },
    "11-20": {
        name: "Feriado - Dia da Consciência Negra",
        description: "Reflexão sobre a luta contra o racismo."
    },
    "12-25": {
        name: "Feriado - Natal",
        description: "Celebração do nascimento de Jesus Cristo."
    }
};

async function loadEvents() {

    try {

        calendarNotes = {}; // LIMPA EVENTOS ANTIGOS

        const response = await fetch(contextPath + '/selectAllEvents');
        const events = await response.json();

        events.forEach(event => {

            const date = event.eventDate;

            calendarNotes[date] = {
                eventName: event.eventName,
                eventText: event.eventText,
                eventStart: event.eventStart,
                eventEnd: event.eventEnd
            };

        });

        renderCalendar()

    } catch (error) {
        console.error("Erro ao carregar eventos:", error);
    }
}
function renderCalendar() {

    calendarElement.innerHTML = '';

    // Week header
    daysOfWeek.forEach(day => {
        const element = document.createElement('div');
        element.className = 'day head';
        element.textContent = day;
        calendarElement.appendChild(element);
    });

    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();

    const monthName = currentDate.toLocaleString('pt-BR', { month: 'long' });
    const formattedMonth =
        monthName.charAt(0).toUpperCase() + monthName.slice(1);

    currentMonthElement.textContent = `${formattedMonth} de ${year}`;

    const firstDayOfMonth = new Date(year, month, 1).getDay();
    const totalDaysInMonth = new Date(year, month + 1, 0).getDate();

    // Empty spaces
    for (let i = 0; i < firstDayOfMonth; i++) {
        calendarElement.appendChild(document.createElement('div'));
    }

    const today = new Date();

    for (let day = 1; day <= totalDaysInMonth; day++) {

        const element = document.createElement('div');
        element.className = 'day';
        element.textContent = day;

        const fullDate = `${year}-${String(month+1).padStart(2,'0')}-${String(day).padStart(2,'0')}`;

        // Show tooltip on hover
        element.addEventListener("mouseenter", (e) => {

            const event = calendarNotes[fullDate];

            // Get holiday using MM-DD format
            const dateKey = `${String(month + 1).padStart(2,'0')}-${String(day).padStart(2,'0')}`;
            const holiday = fixedHolidays[dateKey];

            // Check if this day is today
            const isToday =
                day === today.getDate() &&
                month === today.getMonth() &&
                year === today.getFullYear();

            // Tooltip content container
            let tooltipContent = "";
            let sectionCount = 0;

            // Helper function to add separator between sections
            const addSeparator = () => {
                if (sectionCount > 0) {
                    tooltipContent += `<hr>`;
                }
                sectionCount++;
            };

            /* TODAY INFORMATION */
            if (isToday) {

                addSeparator();

                const todayFormatted = today.toLocaleDateString('pt-BR');

                tooltipContent += `
            <div class="tooltip-title">Hoje</div>
            <div class="tooltip-text">${todayFormatted}</div>
        `;
            }

            /* HOLIDAY INFORMATION */
            if (holiday) {

                addSeparator();

                tooltipContent += `
            <div class="tooltip-title">${holiday.name}</div>
            <div class="tooltip-text">${holiday.description}</div>
        `;
            }

            /* EVENT INFORMATION */
            if (event) {

                addSeparator();

                tooltipContent += `
            <div class="tooltip-title">${event.eventName}</div>
            <div class="tooltip-text">${event.eventText || ""}</div>

            <div class="tooltip-time">
                <span>Início: ${event.eventStart || "--:--"}</span>
                <span>Fim: ${event.eventEnd || "--:--"}</span>
            </div>
        `;
            }

            // Do not show tooltip if there is no content
            if (!tooltipContent) return;

            tooltip.innerHTML = tooltipContent;

            tooltip.classList.add("show");

            // Default tooltip position near the mouse
            let left = e.pageX + 10;
            let top = e.pageY + 10;

            const rect = tooltip.getBoundingClientRect();

            // Prevent tooltip from going off screen (right side)
            if (left + rect.width > window.innerWidth) {
                left = e.pageX - rect.width - 10;
            }

            // Prevent tooltip from going off screen (bottom)
            if (top + rect.height > window.innerHeight) {
                top = e.pageY - rect.height - 10;
            }

            tooltip.style.left = left + "px";
            tooltip.style.top = top + "px";
        });

        // Hide tooltip
        element.addEventListener("mouseleave", () => {
            tooltip.classList.remove("show");
        });

        // Highlight today
        if (
            day === today.getDate() &&
            month === today.getMonth() &&
            year === today.getFullYear()
        ) {
            element.classList.add('today');
        }

        // Holidays
        const dateKey = `${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
        if (fixedHolidays[dateKey]) {
            element.classList.add('holiday');
        }

        // If note exists
        if (calendarNotes[fullDate]) {
            element.classList.add('note');
        }

        // Click event
        element.onclick = () => {

            selectedDate = fullDate;

            const event = calendarNotes[selectedDate];

            if (event) {

                eventPopupTitle.textContent = "Editar evento";

                eventNameInput.value = event.eventName;
                calendarNoteText.value = event.eventText;
                eventStartInput.value = event.eventStart || "";
                eventEndInput.value = event.eventEnd || "";

                deleteNoteBtn.textContent = "Excluir";

            } else {

                eventPopupTitle.textContent = "Novo evento";

                eventNameInput.value = "";
                calendarNoteText.value = "";
                eventStartInput.value = "";
                eventEndInput.value = "";

                deleteNoteBtn.textContent = "Cancelar";
            }

            noteOverlay.classList.add("show");
        };

        calendarElement.appendChild(element);
    }
}

noteOverlay.addEventListener("click", (e) => {
    if (e.target === noteOverlay) {
        noteOverlay.classList.remove("show");
    }
});

// Save note
saveNoteBtn.onclick = () => {

    const name = eventNameInput.value.trim();
    const text = calendarNoteText.value.trim();
    const eventStart = eventStartInput.value.trim();
    const eventEnd = eventEndInput.value.trim();

    if (eventStart > eventEnd) {
        showToast(
            'student',
            'error',
            'Horários inválidos',
            'O horário de início não pode ser maior ou igual ao horário de término.'
        );
        return;
    }

    if (!name.trim()) {
        showToast(
            'student',
            'error',
            'Nome obrigatório',
            'Preencha o campo nome.'
        );
        return;
    }

    // Limit
    if (name.length > 30) {
        showToast(
            'student',
            'error',
            'Nome muito longo',
            'O nome do evento pode ter no máximo 30 caracteres.'
        );
        return;
    }

    const nameWords = name.split(/\s+/);
    for (let word of nameWords) {
        if (word.length > 20) {
            showToast(
                'student',
                'error',
                'Nome inválido',
                'No nome do evento cada palavra pode ter no máximo 20 caracteres.'
            );
            return;
        }
    }

    if (text.length > 140) {
        showToast(
            'student',
            'error',
            'Observação muito longa',
            'A observação pode ter no máximo 140 caracteres.'
        );
        return;
    }

    const textWords = text.split(/\s+/);
    for (let word of textWords) {
        if (word.length > 13) {
            showToast(
                'student',
                'error',
                'Observação inválida',
                'Na observação cada palavra pode ter no máximo 13 caracteres.'
            );
            return;
        }
    }

    confirmEventTitle.textContent = "Salvar este evento?";
    eventAction = "save";

    confirmEventSend.classList.remove("btn-delete");
    confirmEventSend.classList.add("btn-save");

    confirmEventOverlay.classList.add("show");
};

// Delete note
deleteNoteBtn.onclick = () => {

    if (!calendarNotes[selectedDate]) {
        noteOverlay.classList.remove("show");
        return;
    }

    confirmEventTitle.textContent = "Excluir este evento?";
    eventAction = "delete";

    confirmEventOverlay.classList.add("show");
};

confirmEventCancel.onclick = () => {
    confirmEventOverlay.classList.remove("show");
};

confirmEventOverlay.addEventListener("click", (e) => {

    if (e.target === confirmEventOverlay) {
        confirmEventOverlay.classList.remove("show");
    }

});

confirmEventSend.onclick = () => {

    if (eventAction === "save") {

        const name = eventNameInput.value.trim();
        const text = calendarNoteText.value.trim();
        const start = eventStartInput.value || null;
        const end = eventEndInput.value || null;

        calendarNotes[selectedDate] = {
            eventName: name,
            eventDate: selectedDate,
            eventStart: start,
            eventEnd: end,
            eventText: text
        };

        document.getElementById("eventDate").value = selectedDate;

        showToast('student','success','Evento salvo','Evento adicionado ao calendário.');

        console.log("eventDate:", selectedDate);

        eventForm.submit();
    }

    if (eventAction === "delete") {

        delete calendarNotes[selectedDate];

        showToast('student','success','Evento removido','O evento foi excluído do calendário.');

        window.location = `deleteEvent?eventDate=${selectedDate}`;
    }

    confirmEventOverlay.classList.remove("show");
    noteOverlay.classList.remove("show");
    loadEvents()
};

// Navigate months
previousMonthButton.onclick = () => {
    currentDate.setMonth(currentDate.getMonth() - 1);
    renderCalendar()
};

nextMonthButton.onclick = () => {
    currentDate.setMonth(currentDate.getMonth() + 1);
    renderCalendar()
};

// First render

loadEvents()
// ***********************************************************************************************************

// Monster click animation

// Select monster image
const monsterImage = document.querySelector('.monster-container img');

// Apply shake animation on click
monsterImage.addEventListener('click', () => {
    monsterImage.classList.remove('shake'); // Remove animation class
    void monsterImage.offsetWidth;          // Force reflow to restart animation
    monsterImage.classList.add('shake');    // Reapply animation class
});


// ***********************************************************************************************************

document.addEventListener('DOMContentLoaded', () => {

    /* HELPERS */

    // Get element by id (supports typo fallback)
    function getEl(idA, idB) {
        return document.getElementById(idA) || (idB ? document.getElementById(idB) : null);
    }

    // Safe event binding
    function safeAdd(el, evt, fn) {
        if (el) el.addEventListener(evt, fn);
    }

    // Open / close overlay
    function openOverlay(ov) {
        if (!ov) return;
        ov.classList.add('show');
        ov.setAttribute('aria-hidden', 'false');
    }

    function closeOverlay(ov) {
        if (!ov) return;
        ov.classList.remove('show');
        ov.setAttribute('aria-hidden', 'true');
    }

    // Check empty input
    function isEmpty(el) {
        return !el || !String(el.value || '').trim();
    }

    /* ELEMENTS */


    // Action cards
    const studentCard = document.querySelector('.action-card.student');
    const teacherCard = document.querySelector('.action-card.teacher2');

    // Overlays
    const studentOverlay = getEl('studentOverylay', 'studentOverlay');
    const teacherOverlay = getEl('teacherOverylay', 'teacherOverlay');
    const confirmStudentOverlay = getEl('confirmStudentOverlay');
    const confirmTeacherOverlay = getEl('confirmTeacherOverlay');

    // Student inputs
    const studentName = getEl('studentName');
    const studentEmail = getEl('studentEmail');
    const studentAdmission = getEl('studentAdmission');
    const studentGrade = getEl('studentGrade');
    const studentPassword = getEl('studentPassword');

    // Teacher inputs
    const teacherUser = getEl('teacherUser');
    const teacherName = getEl('teacherName');
    const teacherSubject = getEl('teacherSubject');
    const teacherPassword = getEl('teacherPassword');

    // Buttons
    const btnCancelStudent = getEl('btnCancelStudent');
    const btnSaveStudent = getEl('btnSaveStudent');
    const confirmStudentCancel = getEl('confirmStudentCancel');
    const confirmStudentSend = getEl('confirmStudentSend');

    const btnCancelTeacher = getEl('btnCancelTeacher');
    const btnSaveTeacher = getEl('btnSaveTeacher');
    const confirmTeacherCancel = getEl('confirmTeacherCancel');
    const confirmTeacherSend = getEl('confirmTeacherSend');

    // Toast containers
    const toastWrapStudent = getEl('toastWrapStudent');
    const toastWrapTeacher = getEl('toastWrapTeacher');

    /* OPEN MODALS */

    safeAdd(studentCard, 'click', () => openOverlay(studentOverlay));
    safeAdd(teacherCard, 'click', () => openOverlay(teacherOverlay));

    /* CLOSE MODALS*/

    safeAdd(btnCancelStudent, 'click', () => closeOverlay(studentOverlay));
    safeAdd(btnCancelTeacher, 'click', () => closeOverlay(teacherOverlay));

    // Click outside modal
    [studentOverlay, teacherOverlay, confirmStudentOverlay, confirmTeacherOverlay].forEach(ov => {
        if (!ov) return;
        ov.addEventListener('click', e => {
            if (e.target === ov) closeOverlay(ov);
        });
    });

    // ESC key closes all
    document.addEventListener('keydown', e => {
        if (e.key === 'Escape') {
            [studentOverlay, teacherOverlay, confirmStudentOverlay, confirmTeacherOverlay]
                .forEach(closeOverlay);
        }
    });

    /* STUDENT FLOW */

    safeAdd(btnSaveStudent, 'click', e => {
        e.preventDefault();

        if (isEmpty(studentName) || isEmpty(studentEmail) || isEmpty(studentAdmission) || isEmpty(studentGrade) || isEmpty(studentPassword)) {
            showToast('student', 'error', 'Campos obrigatórios', 'Preencha todos os campos antes de continuar.');
            return;
        } else if (!regexPassword.test(studentPassword.value)) {
            showToast('student', 'error', 'Senha inválida', 'Sua senha precisa conter pelo menos um número, uma letra e um caractere especial.');
            return;
        } else if (!regexEmail.test(studentEmail.value)) {
            showToast('student', 'error', 'Formato de e-mail inválido', 'Digite um e-mail válido no formato: nome@dominio.com (ex: joao.silva@gmail.com)');
            return;
        }

        openOverlay(confirmStudentOverlay);
    });

    safeAdd(confirmStudentCancel, 'click', () =>
        closeOverlay(confirmStudentOverlay)
    );

    safeAdd(confirmStudentSend, 'click', () => {
        studentForm.submit();
    });

    /* TEACHER FLOW */

    safeAdd(btnSaveTeacher, 'click', e => {
        e.preventDefault();

        if (isEmpty(teacherUser) || isEmpty(teacherName) || isEmpty(teacherPassword) || isEmpty(teacherSubject)) {
            showToast('teacher', 'error', 'Campos obrigatórios', 'Preencha todos os campos antes de continuar.');
            return;
        } else if (!regexPassword.test(teacherPassword.value)) {
            showToast('teacher', 'error', 'Senha inválida', 'Sua senha precisa conter pelo menos um número, uma letra e um caractere especial.');
            return;
        } else if (!regexUsername.test(teacherUser.value)) {
            showToast('teacher', 'error', 'Nome de usuário inválido', 'O nome de usuário deve seguir o formato: nome.matéria (ex: joao.mat).'
            );
            return;
        }

        openOverlay(confirmTeacherOverlay);
    });

    safeAdd(confirmTeacherCancel, 'click', () =>
        closeOverlay(confirmTeacherOverlay)
    );

    safeAdd(confirmTeacherSend, 'click', () => {
        professorForm.submit();
    });

    /* PASSWORD TOGGLE */

    document.querySelectorAll('.toggle-password').forEach(btn => {
        btn.addEventListener('click', () => {
            const input = btn.previousElementSibling;
            if (!input) return;

            const img = btn.querySelector('img');

            const isHidden = input.type === 'password';

            input.type = isHidden ? 'text' : 'password';

            img.src = isHidden
                ? contextPath + '/assets/eye-off.svg'
                : contextPath + '/assets/eye.svg';

            img.alt = isHidden
                ? 'Hide password'
                : 'Show password';
        });
    });

    /* TOAST SYSTEM */

    function showToast(typeOwner, type, title, message) {
        const wrap =
            typeOwner === 'student' ? toastWrapStudent :
                typeOwner === 'teacher' ? toastWrapTeacher :
                    null;

        if (!wrap) return;

        const toast = document.createElement('div');
        toast.className = `toast ${type}`;

        const icon =
            type === 'error'
                ? contextPath + '/assets/error-icon.svg'
                : contextPath + '/assets/check-icon.svg';

        toast.innerHTML = `
        <img src="${icon}" style="width:30px;height:22px">
        <div class="toast-text">
        <h4>${title}</h4>
        ${message ? `<p>${message}</p>` : ''}
        </div>
    `;

        wrap.appendChild(toast);


        setTimeout(() => {
            toast.classList.add('hide');
            setTimeout(() => toast.remove(), 300);
        }, 4000);
    }

    window.showToast = showToast;
});
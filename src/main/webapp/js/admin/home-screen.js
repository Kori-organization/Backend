// Calendar elements
const calendarElement = document.getElementById('calendar');
const currentMonthElement = document.getElementById('current-month');
const previousMonthButton = document.getElementById('previous-month');
const nextMonthButton = document.getElementById('next-month');

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

// Days of the week
const daysOfWeek = ['DOM', 'SEG', 'TER', 'QUA', 'QUI', 'SEX', 'SAB'];

// Fixed Brazilian holidays
const fixedHolidays = [
    '01-01',
    '04-21',
    '05-01',
    '09-07',
    '10-12',
    '11-02',
    '11-15',
    '11-20',
    '12-25'
];

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

        const fullDate = `${year}-${month + 1}-${day}`;

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
        if (fixedHolidays.includes(dateKey)) {
            element.classList.add('holiday');
        }

        // If note exists
        if (calendarNotes[fullDate]) {
            element.classList.add('note');
        }

        // Click event
        element.onclick = () => {

            selectedDate = fullDate;

            if (calendarNotes[selectedDate]) {

                calendarNoteText.value = calendarNotes[selectedDate];

                deleteNoteBtn.textContent = "Excluir";

            } else {

                calendarNoteText.value = "";

                deleteNoteBtn.textContent = "Cancelar";

            }

            noteOverlay.classList.add("show");
        };

        calendarElement.appendChild(element);
    }
}

// Save note
saveNoteBtn.onclick = () => {

    const text = calendarNoteText.value.trim();

    if (text !== "") {
        calendarNotes[selectedDate] = text;
    }

    noteOverlay.classList.remove("show");

    renderCalendar();

    window.location = `salveEvent?note=${text}&admin=${adminName}&date=${selectedDate}`;
};

// Delete note
deleteNoteBtn.onclick = () => {

    delete calendarNotes[selectedDate];

    noteOverlay.classList.remove("show");

    renderCalendar();
};

// Navigate months
previousMonthButton.onclick = () => {
    currentDate.setMonth(currentDate.getMonth() - 1);
    renderCalendar();
};

nextMonthButton.onclick = () => {
    currentDate.setMonth(currentDate.getMonth() + 1);
    renderCalendar();
};

// First render
renderCalendar();

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
// Calendar elements
const calendarElement = document.getElementById('calendar');
const currentMonthElement = document.getElementById('current-month');
const previousMonthButton = document.getElementById('previous-month');
const nextMonthButton = document.getElementById('next-month');

// Tooltip
const tooltip = document.getElementById("calendarTooltip");

// Current date
let currentDate = new Date();

// EVENTS
let calendarNotes = {};

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


// LOAD EVENTS FROM BACKEND
async function loadEvents() {

    try {

        calendarNotes = {};

        const response = await fetch(contextPath + '/selectAllEvents');
        const events = await response.json();

        events.forEach(event => {

            calendarNotes[event.eventDate] = {
                eventName: event.eventName,
                eventText: event.eventText,
                eventStart: event.eventStart,
                eventEnd: event.eventEnd
            };

        });

        renderCalendar();

    } catch (error) {

        console.error("Erro ao carregar eventos:", error);

    }

}


function renderCalendar() {

    calendarElement.innerHTML = '';

    // Header days
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

    for (let i = 0; i < firstDayOfMonth; i++) {
        calendarElement.appendChild(document.createElement('div'));
    }

    const today = new Date();

    for (let day = 1; day <= totalDaysInMonth; day++) {

        const element = document.createElement('div');
        element.className = 'day';
        element.textContent = day;

        const fullDate =
            `${year}-${String(month+1).padStart(2,'0')}-${String(day).padStart(2,'0')}`;

        const event = calendarNotes[fullDate];

        const dateKey =
            `${String(month + 1).padStart(2,'0')}-${String(day).padStart(2,'0')}`;

        const holiday = fixedHolidays[dateKey];

        const isToday =
            day === today.getDate() &&
            month === today.getMonth() &&
            year === today.getFullYear();

        if (isToday) {
            element.classList.add('today');
        }

        if (holiday) {
            element.classList.add('holiday');
        }

        if (event) {
            element.classList.add('note');
        }

        /* TOOLTIP */

        element.addEventListener("mouseenter", (e) => {

            let tooltipContent = "";
            let sectionCount = 0;

            const addSeparator = () => {
                if (sectionCount > 0) {
                    tooltipContent += `<hr>`;
                }
                sectionCount++;
            };

            // TODAY
            if (isToday) {

                addSeparator();

                const todayFormatted =
                    today.toLocaleDateString('pt-BR');

                tooltipContent += `
                <div class="tooltip-title">Hoje</div>
                <div class="tooltip-text">${todayFormatted}</div>
                `;
            }

            // HOLIDAY
            if (holiday) {

                addSeparator();

                tooltipContent += `
                <div class="tooltip-title">${holiday.name}</div>
                <div class="tooltip-text">${holiday.description}</div>
                `;
            }

            // EVENT
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

            if (!tooltipContent) return;

            tooltip.innerHTML = tooltipContent;

            tooltip.classList.add("show");

            let left = e.pageX + 10;
            let top = e.pageY + 10;

            const rect = tooltip.getBoundingClientRect();

            if (left + rect.width > window.innerWidth) {
                left = e.pageX - rect.width - 10;
            }

            if (top + rect.height > window.innerHeight) {
                top = e.pageY - rect.height - 10;
            }

            tooltip.style.left = left + "px";
            tooltip.style.top = top + "px";

        });

        element.addEventListener("mouseleave", () => {
            tooltip.classList.remove("show");
        });

        calendarElement.appendChild(element);
    }
}

// Navigate months
previousMonthButton.onclick = () => {
    currentDate.setMonth(currentDate.getMonth() - 1);
    renderCalendar();
};

nextMonthButton.onclick = () => {
    currentDate.setMonth(currentDate.getMonth() + 1);
    renderCalendar();
};

// FIRST LOAD
document.addEventListener("DOMContentLoaded", () => {
    loadEvents();
});

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


// *****************************************
// Ranking chart system

// Ranking data
const ranking = window.dataSystem.ranking;

// Max bar height
const maxHeight = 70;

// Render chart
function renderRanking() {

    ranking.forEach((item, index) => {

        const bar =
            document.getElementById("bar" + (index + 1));

        const value =
            document.getElementById("value" + (index + 1));

        // Set value text
        value.textContent = item.average.toFixed(1);

        // Calculate height
        const height =
            (item.average / 10) * maxHeight;

        // Apply height
        bar.style.height =
            height + "px";

    });

    // Find lowest class
    let lowest = ranking[0];

    ranking.forEach(item => {

        if (item.average < lowest.average) {

            lowest = item;

        }

    });

    // Show message
    document.getElementById("rankingText").innerHTML =
        `A turma que precisa de mais atenção na sua disciplina é o <strong>${lowest.class}</strong>.`;

}

// Run
renderRanking();
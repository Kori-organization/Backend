// Calendar elements
const calendarElement = document.getElementById('calendar');
const currentMonthElement = document.getElementById('current-month');
const previousMonthButton = document.getElementById('previous-month');
const nextMonthButton = document.getElementById('next-month');

// Current date
let currentDate = new Date();

// Days of the week
const daysOfWeek = ['DOM', 'SEG', 'TER', 'QUA', 'QUI', 'SEX', 'SAB'];

// Fixed Brazilian holidays
const fixedHolidays = [
    '01-01', // New Year's Day
    '04-21', // Tiradentes
    '05-01', // Labor Day
    '09-07', // Independence Day
    '10-12', // Our Lady of Aparecida
    '11-02', // All Souls' Day
    '11-15', // Republic Proclamation
    '11-20', // Black Awareness Day
    '12-25'  // Christmas
];

function renderCalendar() {
    // Clear the calendar container
    calendarElement.innerHTML = '';

    // Create calendar header
    daysOfWeek.forEach(day => {
        const element = document.createElement('div');
        element.className = 'day head';
        element.textContent = day;
        calendarElement.appendChild(element);
    });

    // Get current year and month
    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();

    // Get month name for the calendar header
    const monthName = currentDate.toLocaleString('pt-BR', { month: 'long' });
    const formattedMonth =
        monthName.charAt(0).toUpperCase() + monthName.slice(1);

    // Update the calendar title (month and year)
    currentMonthElement.textContent = `${formattedMonth} de ${year}`;

    // Get the first weekday of the month
    const firstDayOfMonth = new Date(year, month, 1).getDay();

    // Get total number of days in the month
    const totalDaysInMonth = new Date(year, month + 1, 0).getDate();

    // Add empty spaces before the first day of the month
    for (let i = 0; i < firstDayOfMonth; i++) {
        calendarElement.appendChild(document.createElement('div'));
    }

    // Get today's date
    const today = new Date();

    // Create calendar day elements
    for (let day = 1; day <= totalDaysInMonth; day++) {
        const element = document.createElement('div');
        element.className = 'day';
        element.textContent = day;

        // Highlight the current day
        if (
            day === today.getDate() &&
            month === today.getMonth() &&
            year === today.getFullYear()
        ) {
            element.classList.add('today');
        }

        // Highlight fixed holidays
        const dateKey = `${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
        if (fixedHolidays.includes(dateKey)) {
            element.classList.add('holiday');
        }

        // Add the day to the calendar
        calendarElement.appendChild(element);
    }
}

// Navigate to the previous month
previousMonthButton.onclick = () => {
    currentDate.setMonth(currentDate.getMonth() - 1);
    renderCalendar();
};

// Navigate to the next month
nextMonthButton.onclick = () => {
    currentDate.setMonth(currentDate.getMonth() + 1);
    renderCalendar();
};

// Calendar render
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

// Calculator functionality

// Calculator input elements
const n1Input = document.getElementById('n1');
const n2Input = document.getElementById('n2');
const resultValue = document.querySelector('.result-value');

// Minimum required average
const MEDIA = 7;

// Limits input value between 0 and 10
function limitarInput(input) {
    let valor = parseFloat(input.value);

    if (isNaN(valor)) return;

    if (valor > 10) input.value = 10;
    if (valor < 0) input.value = 0;
}

// Calculate the required score to reach the average
function calcular() {
    // Validate input values
    limitarInput(n1Input);
    limitarInput(n2Input);

    const n1 = parseFloat(n1Input.value);
    const n2 = parseFloat(n2Input.value);

    // Clear result if inputs are invalid
    if (isNaN(n1) || isNaN(n2)) {
        resultValue.textContent = '';
        return;
    }

    const soma = n1 + n2;
    const mediaAtual = soma / 2;

    // If average is already enough, show zero
    if (mediaAtual >= MEDIA) {
        resultValue.textContent = '0';
    } else {
        // Calculate how many points are missing
        const falta = (MEDIA * 2 - soma).toFixed(1);
        resultValue.textContent = falta;
    }
}

// Recalculate whenever the user types
n1Input.addEventListener('input', calcular);
n2Input.addEventListener('input', calcular);
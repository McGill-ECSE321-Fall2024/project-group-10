window.addEventListener('DOMContentLoaded', () => {
const createButton = document.getElementById('create-btn');
createButton.addEventListener('click', () => {
    const name = document.getElementById('name-input').value;
    const date = document.getElementById('date-input').value;
    const regLimit = document.getElementById('reg-limit-input').value;
    const tr = document.createElement('tr');
    tr.innerHTML =`<td>${name}</td><td>${date}</td><td>${regLimit}</td>`;
    const tbody = document.getElementById('event-table-body');
    tbody.appendChild(tr);

})
})
<template>
    <main>
        <h1>Event Registration</h1>
        <h2>New Event</h2>
        <div>
            <select>
                <option value="IN_PERSON">In person</option>
                <option value="ONLINE">Online</option>
            </select>
            <input type="text" placeholder="Name" />
            <input type="date" placeholder="Date" />
            <input type="time" placeholder="Start Time" />
            <input type="time" placeholder="End Time" />
            <input type="text" placeholder="Registration Limit" />
            <input type="text" placeholder="Location" />
            <button>Create Event</button>
            <button class="danger-btn">Clear</button>
        </div>
        <h2>Events</h2>
        <table>
            <tbody>
                <tr>
                    <th>Name</th>
                    <th>Date</th>
                    <th>Type</th>
                </tr>
                <tr>
                    <td>My In-Person Event</td>
                    <td>2024-11-15</td>
                    <td>In person</td>
                </tr>
                <tr>
                    <td>My Online Event</td>
                    <td>2024-11-15</td>
                    <td>Online</td>
                </tr>
            </tbody>
        </table>
    </main>
</template>

<script>
</script>

<style>
main {
    display: flex;
    flex-direction: column;
    align-items: stretch;
}

h2 {
    padding-top: 1em;
    text-decoration: underline;
}

table {
    border-collapse: collapse;
}

td, th {
	border: 1px solid var(--color-border);
	padding: 0.25em;
}

.danger-btn {
    border: 1px solid red;
    color: red;
}
</style>
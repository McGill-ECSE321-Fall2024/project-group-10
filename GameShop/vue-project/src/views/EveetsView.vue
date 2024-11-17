<script>
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

</script>

<template>
<div> 
    <!-- input fields -->
     <!-- type is what the type are we expecitng to have in it  -->
      <!-- palceholder is what we see if it s empty -->
    <input id="name-input"type="text" placeholder="Event Name"/>
    <input type="date" placeholder="Date" id="date-input"/>
    <input type="time" placeholder="Start  Time" id="start-time-input"/>
    <input type="time" placeholder="End Time"/>
    <input type="text" placeholder="Registration Limit" id="reg-limit-input"/>
    <input type="text" placeholder="Location"/>
    <!-- <select type="int" /> -->

    <!-- buttons -->
     <button id="create-btn">Create event</button>
     <button class="notice-btn" id="clear-btn">clear</button>
     <button class="notice-btn" id="del-all-btn">Delete all event</button>

     <h2> Events</h2>
     <table>
        <tbody id="event-table-body">
            <!-- tr is the rows -->
            <tr>
                 <!-- <table head></table> -->
                

                <th> Name</th>
                <th>Date</th>
                <th>Registration Limit</th>

            </tr>
            <tr>
                <td>Party</td>
                <td>1996-03-20</td>
                <td>10</td>
            </tr>
            <tr v-for="e in events



        </tbody>
     </table>

</div>

</template>
<style>
html {
    background-color: wheat;
    font-family: 'Arial', sans-serif;
}
/* we havent linked the css yet ot the html */

h2 {
    text-decoration: underline;
}
td, th {
    padding: 1em;
    /* space between the elements */
    /* relative scale ot the padding scale relatively whatever you wanna use ot the pading  */
    border: 1px solid black;
    /* border around the elements */

}
/* how to only select the button o create event i don t wnat all of them
 */
 /* this hashtag is per id */
#create-btn{
    border: 1px solid green;
    color: green;

}
/* category as a whole uses . */
.notice-btn{
    border: 1px solid red;
    color: red;
}

img{
    width: 400px;
    height: 400px;
    /* width and height of the image */
    border-radius: 100px;
    /* border radius of the image */
}


</style>
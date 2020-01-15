
showAllInvites();
showAllGroups();
showNumberOfWorkoutInvites();
document.getElementById("sendbutton").addEventListener("click", sendInvite)
function run() {
    console.log("hej")
}
function showNumberOfWorkoutInvites(value) {
    let arg1 = document.getElementById("bodyTest").getAttribute('data-arg1')
    if(value == -1) {
        arg1 = arg1-1;
    }
    let container = document.getElementById("showNumberOfWorkoutInvites")
    if(arg1 == 1) {
        container.innerHTML = `Woop woop! You have 1 new workout.`
    }
    else if(arg1 > 1) {
        container.innerHTML = `Woop woop! You have ${arg1} new workouts.`
    }
    else 
    container.innerHTML = `I´m sorry, no new workouts yet.. You can create your own if you'd like.`
}
function declineInviteWorkout () {
    let arg1 = event.target.getAttribute('data-arg1');
    getmappingDeclineInviteWorkout("http://localhost:8081/rest/declineWorkoutInvite/" + arg1)
    console.log("workoutInviteContainer" + arg1);
    let container = document.getElementById("workoutInviteContainer" + arg1)
    container.innerHTML = ` <div class="modal-body mt-0"><h6 class="modal-title">Yeah, you´re right, this workout wasn´t really you.</h6></div>`
    updateWorkoutInvite("http://localhost:8081/rest/updateWorkoutInvite");
    showNumberOfWorkoutInvites(-1);
    setTimeout( () => test(container), 1000)

}

function test(container) {
    container.innerHTML = ``
}

function acceptInviteWorkout () {
    let arg1 = event.target.getAttribute('data-arg1');
    getmappingAcceptInviteWorkout("http://localhost:8081/rest/acceptWorkoutInvite/" + arg1)
    let container = document.getElementById("workoutInviteContainer" + arg1)
    container.innerHTML = ` <div class="modal-body mt-0"> <h6 class="modal-title">Yes, you'll crush this one!</h6> </div>`
    updateWorkoutInvite("http://localhost:8081/rest/updateWorkoutInvite");
    showNumberOfWorkoutInvites(-1);
    setTimeout( () => test(container), 1000)
}



async function declineButtonfunction (value) {
    console.log("Acessed declinebutton function.")
    console.log("value " + value)
    await declineInvite("http://localhost:8081/rest/declineInvite/" + value)
    console.log("I think you deleted a invite now..")
    showAllInvites();
}
async function acceptButtonFunction (value) {
    console.log("Accesed accept button function")
    console.log("value " + value)
    await acceptInvite("http://localhost:8081/rest/acceptInvite/" + value)
    showAllInvites();
    showAllGroups();
}

async function showAllInvites() {
    let array = await getAllInvites("http://localhost:8081/rest/getAllInvites")
    let container = document.getElementById("inviteContainer")
    let textToPrint = "";
    if(array[0].length > 0) {
        textToPrint += `<button type="button" id="showNumberOfWorkoutInvites" th:data-arg1="${invitesToWorkoutLength}" class="btn btn-block color-medium mt-3 mb-3 pt-3 pb-3 button-add-workout" data-toggle="modal" data-target="#newinvite"></button>
        <div class="modal fade" th:id="newinvite" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">YOUR NEW GROUP INVITES</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>  
            </div> 
            <div class="modal-body mt-0">
             `
    for(let i = 0; i < array[1].length; i++) {
        console.log("Invite id should be: " + array[0][i].id)
        textToPrint +=
    `                
                        <h5 th:text="${array[2][i]} + ' sent you this nice invite."></h5>
                        <h5 th:text="${array[1][i]} + ' sent you this nice invite."></h5>
                        <!--With th:data-arg1 skickas en variabel till den benämnda JS funktionen. -->
                        <button class="btn btn-sm button-add-user mt-2 mt-5" th:data-arg1="${array[0][i].id}"  id="acceptButton${array[0][i].id}" value="${array[0][i].id}">ACCEPT</button>
                        <button class="btn btn-sm button-add-user mt-2 mt-5" th:data-arg1="${array[0][i].id}" id="declineButton${array[0][i].id}" value="${array[0][i].id}">DECLINE</button>
                    
    
     `

    document.getElementById("declineButton"+array[0][i].id).addEventListener("click", nestingFunction = () => {
        declineButtonfunction(document.getElementById("declineButton" +array[0][i].id).value) })
    document.getElementById("acceptButton"+array[0][i].id).addEventListener("click", nestingFunction = () => {
        acceptButtonFunction(document.getElementById("acceptButton" +array[0][i].id).value)

    })
        }
        textToPrint += `</div>
        </div>
    </div>
</div>
</div>
</div>`
container.innerHTML = textToPrint;

    }
    else
    container.innerHTML = 
    `<h2> You have no invite girlfriend </h2>`
}
async function showAllGroups(){
    let array = await getAllGroups("http://localhost:8081/rest/getAllGroups")
    let container = document.getElementById("groupContainer")
    let textToPrint = "";
   
    if(array.length > 0) {
        for(let i = 0; i < array.length; i++) {
            let membersInGroup = await getAllMembersInGroup("http://localhost:8081/rest/getAllGroupMembers/" + array[i].id)
            console.log("username of membersingroup " + membersInGroup[i].username)

          


            textToPrint += `<a class="color-medium" href="/group/${array[i].id}">
            <div class="card color-medium cardGroup">
                <div class="card-body">
                    <h4 class="card-title">${array[i].name.toUpperCase()}</h4>
                    <hr/>
                    <p class="card-text">${array[i].description}</p>
                    <div class="members">
                        <hr/>
                        <h5 class="members-headline">Members</h5>
                        <ul class="list-group list-group-flush">`
                    for(let a = 0; a < membersInGroup.length; a++) {
                         textToPrint += `<li class="list-group-item">${membersInGroup[a].username}</li>`
                    }
                    textToPrint += `
                    </ul>
                    </div>
                    </div>
                    </div>
                    </a>`                   

        }
    
            container.innerHTML = textToPrint;
             
        }
        else
        container.innerHTML = 
        `<h2> You have no groups girlfriend </h2>`
}


async function sendInvite () {
//Spara värdet
    let memberName = document.getElementById("membername").value;
    let result = await getInformation("http://localhost:8081/rest/getUser/" + memberName)
    console.log("membername: " + memberName);

    let responseText = "";
    let responseTitle ="";
    if(result.username !== null ){
        responseTitle = "Successful!"
        responseText = "An invite has been sent to: " + memberName;
        
    } else {
        responseTitle = "Unsuccessful"
        responseText = "There is no member with username: " + memberName;
    }
    
    sendNewModal(responseTitle, responseText);
}

function sendNewModal(responseTitle, responseText){
    let newModal = document.getElementById("newModal");

    newModal.innerHTML = `<h1>${responseText}</h1>`
}

async function getInformation(url) {
    let response = await fetch(url, {
        method: "GET"
    });
    if (response.status === 200) {
        let result = await response.json()
        console.log(result)
        //var objekt = JSON.parse(result)
        return result
    }
    else console.log("unexpected error", response)
}

async function declineInvite(url) {
    let response = await fetch(url, {
        method: "DELETE"
    });
    if (response.status === 200) {
        console.log("Deleted" + url)
    }
    else console.log("unexpected error", response)
}

async function acceptInvite(url) {
    let response = await fetch(url, {
        method: "GET"
    });
    if (response.status === 200) {
        let result = await response.json()
        console.log(result)
        //var objekt = JSON.parse(result)
        return result
    }
    else console.log("unexpected error ", response)
}

async function getAllInvites(url) {
    let response = await fetch(url, {
        method: "GET"
    });
    if (response.status === 200) {
        let result = await response.json()
        console.log(result)
        //var objekt = JSON.parse(result)
        return result
    }
    else console.log("unexpected error", response)
}


async function getAllGroups(url){
    let response = await fetch(url, {
        method: "GET"
    });
    if (response.status === 200) {
        let result = await response.json()
        console.log(result)
        //var objekt = JSON.parse(result)
        return result
    }
    else console.log("unexpected error", response)

}


async function getAllMembersInGroup(url) {
    let response = await fetch(url, {
        method: "GET"
    });
    if (response.status === 200) {
        let result = await response.json()
        console.log(result)
        //var objekt = JSON.parse(result)
        return result
    }
    else console.log("unexpected error", response)
}

async function getmappingAcceptInviteWorkout(url) {
    let response = await fetch(url, {
        method: "GET"
    });
    if (response.status === 200) {
        console.log("Sucess in acceptInviteWorkout")
    }
    else console.log("unexpected error", response)
}

async function getmappingDeclineInviteWorkout(url) {
    let response = await fetch(url, {
        method: "GET"
    });
    if (response.status === 200) {
        console.log("Sucess in declineInviteWorkout")
    }
    else console.log("unexpected error", response)
}

async function updateWorkoutInvite(url) {
    let response = await fetch(url, {
        method: "GET"
    });
    if (response.status === 200) {
        console.log("Sucess in updateWorkoutInvite")
    }
    else console.log("unexpected error", response)
}

function editexercise(){
    let value = event.target.getAttribute('data-arg1');
    console.log("value from data-arg1 " + value)
    editexercisegetmapping("http://localhost:8081/rest/editExercise/"+ value)
}

async function editexercisegetmapping(url) {
    let response = await fetch(url, {
        method: "GET"
    });
    if (response.status === 200) {
        console.log("Sucess in editexecisegetmapping")
    }
    else console.log("unexpected error", response)
}
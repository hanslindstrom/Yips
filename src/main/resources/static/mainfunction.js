showAllGroups();
showNumberOfGroupInvites();
showNumberOfWorkoutInvites();


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

function acceptInviteGroup () {
    let arg1 = event.target.getAttribute('data-arg1');
    getmappingAcceptInviteGroup("http://localhost:8081/rest/acceptInviteGroup/" + arg1)
    let container = document.getElementById("groupInviteContainer" + arg1)
    container.innerHTML = ` <div class="modal-body mt-0"> <h6 class="modal-title">These are your new buddies, get along now!</h6> </div>`
    showNumberOfGroupInvites(true);
    setTimeout( () => emptyClickedInvite(container), 3000)
    setTimeout( () => showAllGroups(), 500)
}

function declineInviteGroup() {
    let arg1 = event.target.getAttribute('data-arg1');
    getmappingDeclineInviteGroup("http://localhost:8081/rest/declineInviteGroup/" + arg1)
    let container = document.getElementById("groupInviteContainer" + arg1)
    container.innerHTML = ` <div class="modal-body mt-0"> <h6 class="modal-title">Yeah I didn´t like those guys either. We will find better friends.</h6> </div>`
    showNumberOfGroupInvites(true);
    setTimeout( () => emptyClickedInvite(container), 3000)
    setTimeout( () => showAllGroups(), 500)
}

function emptyClickedInvite (container) {
    container.innerHTML = ``
}

function showNumberOfGroupInvites(isLower) {
    let arg1 = document.getElementById("groupInvite").getAttribute('data-arg1')
    console.log("We want to decrease invites sent. " + isLower + " which currently is " + arg1)
    if(isLower == true) {
        arg1 = arg1-1;
    }
    console.log("Now arg1 is " + arg1)
    let container = document.getElementById("showGroupInvites")
    if(arg1 == 1) {
        container.innerHTML = `Woop woop! You have 1 new group invite.`
    }
    else if(arg1 > 1) {
        container.innerHTML = `Woop woop! You have ${arg1} new group invites.`
    }
    else 
    container.innerHTML = `I´m sorry, no new group invites yet.. You can create your own group if you'd like.`
}





async function showAllGroups(){
    let array = await getAllGroups("http://localhost:8081/rest/getAllGroups")
    let container = document.getElementById("groupContainer")
    let textToPrint = "";
   
    if(array.length > 0) {
        for(let i = 0; i < array.length; i++) {
            let id = array[i].id
            console.log(" id sent with getallgroupmembers " + id)
           

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
                        let membersInGroup = getAllMembersInGroup("http://localhost:8081/rest/getAllGroupMembers/" + id)
                        setTimeout(function() {console.log("Maybe this works...")}, 300)
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

async function getmappingAcceptInviteGroup(url) {
    let response = await fetch(url, {
        method: "GET"
    });
    if (response.status === 200) {
        console.log("Sucess in acceptinvitegroup")
    }
    else console.log("unexpected error", response)
}

async function getmappingDeclineInviteGroup(url) {
    let response = await fetch(url, {
        method: "GET"
    });
    if (response.status === 200) {
        console.log("Sucess in acceptinvitegroup")
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
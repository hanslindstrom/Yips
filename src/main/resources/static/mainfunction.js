var numberOfWorkoutInvites = document.getElementById("bodyTest").getAttribute('data-arg1')
showAllGroups();
showNextWorkout();
showMostRecentWorkout();
showNumberOfGroupInvites();
showNumberOfWorkoutInvites();


function showNumberOfWorkoutInvites(value) {
    console.log("number of workoutinvites are " + numberOfWorkoutInvites)
    if(value == -1) {
        numberOfWorkoutInvites = numberOfWorkoutInvites-1;
        console.log("we are lowering numberofworkoutsinvites " + numberOfWorkoutInvites)
    }
    let container = document.getElementById("showNumberOfWorkoutInvites")
    if(numberOfWorkoutInvites == 1) {
        container.innerHTML = `Woop woop! You have 1 new workout.`
    }
    else if(numberOfWorkoutInvites > 1) {
        container.innerHTML = `Woop woop! You have ${numberOfWorkoutInvites} new workouts.`
    }
    else 
    container.innerHTML = `No new workouts, maybe create one for yourself?`
}
function declineInviteWorkout () {
    let arg1 = event.target.getAttribute('data-arg1');
    getmappingDeclineInviteWorkout("http://localhost:8081/rest/declineWorkoutInvite/" + arg1)
    console.log("workoutInviteContainer" + arg1);
    let container = document.getElementById("workoutInviteContainer" + arg1)
    container.innerHTML = ` <div class="modal-body mt-0"><h6 class="modal-title">Yeah, you´re right, this workout wasn´t really you.</h6></div>`
    setTimeout(() => updateWorkoutInvite("http://localhost:8081/rest/updateWorkoutInvite"), 1000)
    setTimeout( () => test(container), 2500)
    setTimeout( () => showNumberOfWorkoutInvites(-1), 2500)
    setTimeout( () =>     showNextWorkout(), 2500  )
}

function test(container) {
    container.innerHTML = ``
}

function acceptInviteWorkout () {
    let arg1 = event.target.getAttribute('data-arg1');
    getmappingAcceptInviteWorkout("http://localhost:8081/rest/acceptWorkoutInvite/" + arg1)
    let container = document.getElementById("workoutInviteContainer" + arg1)
    container.innerHTML = ` <div class="modal-body mt-0"> <h6 class="modal-title">Yes, you'll crush this one!</h6> </div>`
    setTimeout(() => updateWorkoutInvite("http://localhost:8081/rest/updateWorkoutInvite"), 1000)
    setTimeout( () => test(container), 2500)
    setTimeout( () => showNumberOfWorkoutInvites(-1), 2500)
    setTimeout( () =>     showNextWorkout(), 2500  )
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
        console.log("Lowering number of group invites")
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
    container.innerHTML = `Sorry, no new group invites..`
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
async function showNextWorkoutSuccess(nextWorkout) {
    
    console.log("Next workout is " + nextWorkout.id + nextWorkout.name)
        console.log("Nextworkout after settimeout " + nextWorkout.id)
    
            let exerciseListNextWorkout = await getExerciseListNextWorkout("http://localhost:8081/rest/getExerciseListNextWorkout/" + nextWorkout.id, nextWorkout)
           
}

async function showMostRecentWorkoutSuccess(doneWorkout) {
    console.log("Done workout is " + doneWorkout.id + doneWorkout.name)
    await getExerciseListMostRecentWorkout("http://localhost:8081/rest/getExerciseListMostRecentWorkout/" + doneWorkout.id, doneWorkout)
}

async function printnextWorkoutSucess(nextWorkout) {
    let container = document.getElementById("nextWorkoutContainer")
    let textToPrint = "";
    console.log("alright, we are printing without exercises.")
    textToPrint += `<div class="col-sm">
            <div class="card color-contrast">
                    <a class="text-color-dark workout-hover" href="workout/${nextWorkout.id}">
                        <div class="card-body">
                            <h4 class="card-title d-flex justify-content-center">YOUR NEXT WORKOUT</h4>
                            <hr/>
                            <h5>${nextWorkout.name}</h5>
                            <p class="card-text">${nextWorkout.description}</p>
                            <hr/> 
                             <hr/> <h5  th:if="*{date != null}" th:text="*{date}">2020-02-02</h5> </div> </a> </div>`    
                    
            
                            container.innerHTML = textToPrint;

}

async function printmostRecentWorkoutSucess(doneWorkout) {
    let container = document.getElementById("mostPreviousWorkoutContainer")
    let textToPrint = "";
    console.log("alright, we are printing without exercises.")
    textToPrint += `<div class="col-sm">
            <div class="card color-contrast">
                    <a class="text-color-dark workout-hover" href="workout/${doneWorkout.id}">
                        <div class="card-body">
                            <h4 class="card-title d-flex justify-content-center">YOUR COMPLETED WORKOUT</h4>
                            <hr/>
                            <h5>${doneWorkout.name}</h5>
                            <p class="card-text">${doneWorkout.description}</p>
                            <hr/> 
                            <hr/> <h5 >${doneWorkout.date}</h5> </div> </a> </div>`    
                    

            
                            container.innerHTML = textToPrint;
}
function showNextWorkoutNull () {
    let container = document.getElementById("nextWorkoutContainer")
    let textToPrint = "";
    textToPrint += `<div class="col-sm">
            <div class="card color-contrast">
                   
                        <div class="card-body">
                            <h4 class="card-title d-flex justify-content-center">YOUR NEXT WORKOUT</h4>
                            <hr/>
                            <h5>It´s time to plan a new workout!</h5>
                            </div>  </div> </div>`
container.innerHTML = textToPrint;                      
}

function showMostRecentWorkoutNull() {
    let container = document.getElementById("mostPreviousWorkoutContainer")
    let textToPrint = "";
    textToPrint += `<div class="col-sm">
            <div class="card color-contrast">
                   
                        <div class="card-body">
                            <h4 class="card-title d-flex justify-content-center">YOUR COMPLETED WORKOUTS</h4>
                            <hr/>
                            <h5>You haven´t completed any yet... But don´t worry, you´ll get there!</h5>
                            </div>  </div>`
container.innerHTML = textToPrint;   

}

async function printExerciseListWithMostRecentWorkoutSuccess(exerciseListMostRecentWorkout, doneWorkout) {
    let container = document.getElementById("mostPreviousWorkoutContainer")
    let textToPrint = "";
    console.log("And the exercise list before the settimeout... " + exerciseListMostRecentWorkout.length)
            
                console.log("And the exercise list is... " + exerciseListMostRecentWorkout.length)

            textToPrint += `<div class="col-sm">
            <div class="card color-contrast">
                    <a class="text-color-dark workout-hover" href="workout/${doneWorkout.id}">
                        <div class="card-body">
                            <h4 class="card-title d-flex justify-content-center">YOUR COMPLETED WORKOUT</h4>
                            <hr/>
                            <h5>${doneWorkout.name}</h5>
                            <p class="card-text">${doneWorkout.description}</p>
                            <hr/>`
                                for(let exercise of  exerciseListMostRecentWorkout) {
                                    console.log("AND WE ARE LOOOPING" + exercise.name)
                                    textToPrint += `<h5 class="mt-3" >${exercise.name}</h5>
                                    <button class="btn btn-sm color-dark mb-3 mt-3 more-info-exercise">More info</button>
                                    <div class="exercisecontainer">`
                                    if(exercise.seconds != 0) {
                                        textToPrint += `<p>${exercise.seconds}</p>`
                                    }
                                    if(exercise.meters != 0) {
                                        textToPrint += `<p>${exercise.meters}</p>`
                                    }
                                    if(exercise.calories != 0) {
                                        textToPrint += `<p>${exercise.calories}</p>`
                                    }
                                    if(exercise.weight != 0) {
                                        textToPrint += `<p>${exercise.weigth}</p>`
                                    }
                                    if(exercise.reps != 0) {
                                        textToPrint += `<p>${exercise.reps}</p>`
                                    }
                                    if(exercise.sets != 0) {
                                        textToPrint += `<p>${exercise.sets}</p>`
                                    }
                                    if(exercise.cadence != 0) {
                                        textToPrint += `<p>${exercise.cadence}</p>`
                                    }
                                    textToPrint += `</div>`
                                }
        
                            
                            
                        textToPrint += `<hr/> <h5  >${doneWorkout.date}</h5> </div> </a> </div>`    
                    
            
            container.innerHTML = textToPrint;
}

async function printExerciseListWithNextWorkoutSuccess(exerciseListNextWorkout, nextWorkout) {
    let container = document.getElementById("nextWorkoutContainer")
    let textToPrint = "";
    console.log("And the exercise list before the settimeout... " + exerciseListNextWorkout.length)
            
                console.log("And the exercise list is... " + exerciseListNextWorkout.length)

            textToPrint += `<div class="col-sm">
            <div class="card color-contrast">
                    <a class="text-color-dark workout-hover" href="workout/${nextWorkout.id}">
                        <div class="card-body">
                            <h4 class="card-title d-flex justify-content-center">YOUR NEXT WORKOUT</h4>
                            <hr/>
                            <h5>${nextWorkout.name}</h5>
                            <p class="card-text">${nextWorkout.description}</p>
                            <hr/>`
                                for(let exercise of  exerciseListNextWorkout) {
                                    console.log("AND WE ARE LOOOPING" + exercise.name)
                                    textToPrint += `<h5 class="mt-3" >${exercise.name}</h5>
                                    <button class="btn btn-sm color-dark mb-3 mt-3 more-info-exercise">More info</button>
                                    <div class="exercisecontainer">`
                                    if(exercise.seconds != 0) {
                                        textToPrint += `<p>${exercise.seconds}</p>`
                                    }
                                    if(exercise.meters != 0) {
                                        textToPrint += `<p>${exercise.meters}</p>`
                                    }
                                    if(exercise.calories != 0) {
                                        textToPrint += `<p>${exercise.calories}</p>`
                                    }
                                    if(exercise.weight != 0) {
                                        textToPrint += `<p>${exercise.weigth}</p>`
                                    }
                                    if(exercise.reps != 0) {
                                        textToPrint += `<p>${exercise.reps}</p>`
                                    }
                                    if(exercise.sets != 0) {
                                        textToPrint += `<p>${exercise.sets}</p>`
                                    }
                                    if(exercise.cadence != 0) {
                                        textToPrint += `<p>${exercise.cadence}</p>`
                                    }
                                    textToPrint += `</div>`
                                }
        
                            
                            
                        textToPrint += `<hr/> <h5  >${nextWorkout.date}</h5> </div> </a> </div>`    
                    
            
            container.innerHTML = textToPrint;

}

async function showNextWorkout(){
    let nextWorkout = await getNextWorkouts("http://localhost:8081/rest/getNextWorkouts")
}
async function showMostRecentWorkout(){
    await getMostRecentWorkout("http://localhost:8081/rest/getMostRecentWorkout")
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

async function getMostRecentWorkout(url) {
    let response = await fetch(url, {
        method: "GET"
    });
    if (response.status === 200) {
        let result = await response.json()
        console.log("getting most recent workout is sucess: " + result.id)
        if(result.id != null) {
            setTimeout( () => showMostRecentWorkoutSuccess(result), 200)
        }
        else
        showMostRecentWorkoutNull();
        return result
    }
    else console.log("unexpected error", response)
}

async function getNextWorkouts(url){
    let response = await fetch(url, {
        method: "GET"
    });
    if (response.status === 200) {
        let result = await response.json()
        console.log("getting next workout is sucess: " + result.id)
        if(result.id != null) {
            console.log("We shoudlnt be in here")
            setTimeout( () => showNextWorkoutSuccess(result), 200)
        }
        else
        showNextWorkoutNull();
        return result
    }
    else console.log("unexpected error", response)

}

async function getExerciseListMostRecentWorkout(url, doneWorkout) {
    let response = await fetch(url, {
        method: "GET"
    });
    if (response.status === 200) {
        let result = await response.json()
        console.log("getting exerciselist for most recent workout is sucess" + result)
        if(result.length > 0) {
            console.log("The list is as long as " + result.length)
            setTimeout( () => printExerciseListWithMostRecentWorkoutSuccess(result, doneWorkout), 200)
        }
        printmostRecentWorkoutSucess(doneWorkout);
        //var objekt = JSON.parse(result)
        return result
    }
    else console.log("unexpected error", response)
}

async function getExerciseListNextWorkout(url, nextWorkout) {
    let response = await fetch(url, {
        method: "GET"
    });
    if (response.status === 200) {
        let result = await response.json()
        console.log("getting exerciselist for next workout is sucess" + result)
        if(result.length > 0) {
            console.log("The list is as long as " + result.length)
            setTimeout( () => printExerciseListWithNextWorkoutSuccess(result, nextWorkout), 200)
        }
        printnextWorkoutSucess(nextWorkout);
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
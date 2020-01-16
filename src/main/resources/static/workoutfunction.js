


let editOrSaveWorkout = document.getElementById("editsaveworkout");
let containersendworkout = document.getElementById("containersendworkout");
let containeraddexercise = document.getElementById("containeraddexercise")

var saveAndSendToGroup = true;

function saveWorkout() {

    if(saveAndSendToGroup) {
        containersendworkout.style="display: none;";
        containeraddexercise.style="display: block;";
        editOrSaveWorkout.innerHTML = "Save workout";
        saveAndSendToGroup = false;

    }

    else {
        
        containersendworkout.style="display: block;";
        containeraddexercise.style="display: none;";
        editOrSaveWorkout.innerHTML = "Edit workout";
        saveAndSendToGroup = true;
        
    }

}
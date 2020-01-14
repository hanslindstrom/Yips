showAllInvites();
showAllGroups();
document.getElementById("sendbutton").addEventListener("click", sendInvite)
document.getElementById("")
function run() {
    console.log("hej")
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
    for(let i = 0; i < array[1].length; i++) {
        console.log("Invite id should be: " + array[0][i].id)
        textToPrint +=
    `<h2>Sender</h2>
    <h4>${array[2][i]}</h4>
    <h2>Group</h2>
    <h4>${array[1][i]}</h4>
    
    <button type="button" class="btn btn-secondary" id="acceptButton${array[0][i].id}" value="${array[0][i].id}" >Accept</button>
    <button type="button" class="btn btn-secondary" id="declineButton${array[0][i].id}" value="${array[0][i].id}" >Decline</button>
     `
     container.innerHTML = textToPrint;

    document.getElementById("declineButton"+array[0][i].id).addEventListener("click", nestingFunction = () => {
        declineButtonfunction(document.getElementById("declineButton" +array[0][i].id).value) })
    document.getElementById("acceptButton"+array[0][i].id).addEventListener("click", nestingFunction = () => {
        acceptButtonFunction(document.getElementById("acceptButton" +array[0][i].id).value)

    })
        }
         
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
            console.log("id " + membersInGroup[0].username)
            console.log("id " + membersInGroup[1].username)

          


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


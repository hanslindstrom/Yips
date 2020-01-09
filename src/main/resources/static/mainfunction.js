showAllInvites();
document.getElementById("sendbutton").addEventListener("click", sendInvite)

function run() {
    console.log("hej")
}

async function declineButtonfunction () {
    console.log("Acessed declinebutton function.")
    await declineInvite("http://localhost:8081/rest/declineInvite/" + document.getElementById("declineButton").value)
    console.log("I think you deleted a invite now..")
    showAllInvites();
}
async function acceptButtonFunction () {
    console.log("Accesed accept button function")
    await acceptInvite("http://localhost:8081/rest/acceptInvite", document.getElementById("acceptButton").value)
    console.log("I think you accepted a invite now..")
    showAllInvites();
}

async function showAllInvites() {
    let array = await getAllInvites("http://localhost:8081/rest/getAllInvites/")
    let container = document.getElementById("inviteContainer")
    if(array[0].length > 0) {
    for(let i = 0; i < array[1].length; i++) {
    container.innerHTML = 
    `<h2>Sender</h2>
    <h4>${array[2][i]}</h4>
    <h2>Group</h2>
    <h4>${array[1][i]}</h4>
    <button type="button" class="btn btn-secondary" id="declineButton" value=${array[0][i].id} >Decline</button>
    <button type="button" class="btn btn-secondary" id="acceptButton" value=${array[0][i]}  >Accept</button>
     `
    document.getElementById("declineButton").addEventListener("click", declineButtonfunction)
    document.getElementById("acceptButton").addEventListener("click", acceptButtonFunction)
        } 
    }
    else
    container.innerHTML = 
    `<h2> You have no invite girlfriend </h2>`
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

async function acceptInvite(url, data) {
    console.log(data)

    let response = await fetch(url, {
        method: "PUT",
        body: data 
    });
    if (response.status === 200) {
        console.log("Accepeted " + url)
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




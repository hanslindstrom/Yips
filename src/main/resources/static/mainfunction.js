document.getElementById("sendbutton").addEventListener("click", sendInvite);
document.getElementById("declineinvite").addEventListener("click", );
document.getElementById("acceptinvite").addEventListener("click",  );

function run() {
    console.log("hej")
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



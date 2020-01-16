document.getElementById("sendbutton").addEventListener("click", sendInvite)


    async function sendInvite () {
    //Spara värdet
    console.log("Accesses sendinvite for send invite")
        let memberName = document.getElementById("membername").value;
        console.log("memberName " + memberName)
    
        let result = await getInformation("http://localhost:8081/rest/getUser/" + memberName)
        console.log("result is " + result.username)
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

    async function getInformation(url) {
        console.log("Accesses getinformation for send invite")
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

    
function sendNewModal(responseTitle, responseText){
    let newModal = document.getElementById("newModal");

    newModal.innerHTML = `<h1>${responseText}</h1>`
}
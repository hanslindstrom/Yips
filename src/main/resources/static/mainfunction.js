document.getElementById("sendbutton").addEventListener("click", sendInvite);

function run() {
    console.log("hej")
}

function sendInvite () {
//Spara värdet
    let memberName = document.getElementById("membername").value;
    let result = getInformation("http://localhost:8081/rest/getUser/" + memberName)
    console.log("membername: " + memberName);
    console.log("result objekt" + result);
    
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

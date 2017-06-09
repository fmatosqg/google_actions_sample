
const client = new ApiAi.ApiAiClient({accessToken: 'df6d953cbfa4429ebf1f79660d810913'});

sendQuestion('delay');

function sendQuestion(question) {
    let promise = client.textRequest(question);

    console.log('question: ' + question);

    promise
        .then(handleResponse)
        .catch(heandleError);
}

function handleResponse(serverResponse) {
//        console.log(serverResponse);
        var answer = serverResponse.result.fulfillment.displayText;
        console.log('answer: ' + answer);
}
function heandleError(serverError) {
        console.log(serverError);
}

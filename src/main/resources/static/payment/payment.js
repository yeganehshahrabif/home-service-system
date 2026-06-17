let time = 600; // 10 minutes

let timer = setInterval(() => {

    let min = Math.floor(time / 60);
    let sec = time % 60;

    document.getElementById("time").innerText =
        `${min}:${sec < 10 ? "0" + sec : sec}`;

    time--;

    if (time < 0) {
        clearInterval(timer);
        alert("Payment time expired");
        document.getElementById("paymentForm").style.display = "none";
    }

}, 1000);


document.getElementById("paymentForm").addEventListener("submit", function(e) {

    e.preventDefault();

    fetch("/api/payment/verify", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            cardNumber: "1234",
            cvv2: "123",
            expiryDate: "12/30",
            password: "123",
            captcha: "abc"
        })
    })
        .then(res => res.json())
        .then(data => {
            document.getElementById("message").innerText =
                data.message;
        });

});
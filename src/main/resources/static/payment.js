let captchaKey = null;

const paymentId = new URLSearchParams(window.location.search).get("paymentId");

function loadCaptcha() {

    fetch("/api/v1/captcha")
        .then(res => res.json())
        .then(data => {

            captchaKey = data.key;

            document.getElementById("captchaImage").src = data.image;
        });
}

loadCaptcha();


let time = 600;

const timer = setInterval(() => {

    let min = Math.floor(time / 60);
    let sec = time % 60;

    document.getElementById("time").innerText =
        `${min}:${sec < 10 ? "0" + sec : sec}`;

    time--;

    if (time < 0) {
        clearInterval(timer);

        document.getElementById("message").innerText =
            "Payment expired ❌";

        document.getElementById("paymentForm").style.display = "none";
    }

}, 1000);


document.getElementById("paymentForm").addEventListener("submit", function (e) {

    e.preventDefault();

    const request = {
        paymentId: paymentId,

        captchaKey: captchaKey,
        captchaInput: document.getElementById("captchaInput").value,

        cardNumber: document.getElementById("cardNumber").value,
        cvv2: document.getElementById("cvv2").value,
        expireDate: document.getElementById("expireDate").value,
        password: document.getElementById("password").value
    };

    fetch("/api/v1/payments/wallet/recharge/confirm", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(request)
    })
        .then(res => res.json())
        .then(data => {

            document.getElementById("message").innerText = data.message;


            loadCaptcha();

        })
        .catch(() => {
            document.getElementById("message").innerText =
                "Server error ❌";
        });
});
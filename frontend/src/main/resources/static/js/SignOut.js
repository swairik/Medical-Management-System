$(document).ready(function () {


    $("#out").on("click", function(e) {
        // Do something when the button is clicked

        e.preventDefault();

        // if(result) {
        // document.cookie = "authToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

        // window.location.href = "Auth";
        // }

        document.cookie = "authToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

        window.location.href = "Auth";
    });
});
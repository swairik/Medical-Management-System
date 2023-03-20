$(document).ready(function () {

    // var myElement = $("#load_content").find("#out");

    // console.log(myElement)

    // myElement.css("background-color", "red");
    
    // // myElement.click(function (e) {
    // //     console.log("clicked");

    // //     e.preventDefault();

    // //     // document.cookie = "authToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

    // //     // // Redirect the user to the login page or a signout confirmation page
    // //     // window.location.href = "/Auth";
    // //   });

    // myElement.on("click", function() {
    //     // Do something when the button is clicked
    //     alert("Button clicked!");
    // });

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
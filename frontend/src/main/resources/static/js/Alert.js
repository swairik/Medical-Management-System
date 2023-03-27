function customAlert(message) {
    
    const alertBox = document.getElementById("custom-alert");
    const alertContent = document.querySelector(".custom-alert-content");
  
    alertContent.textContent = message;
  
    alertBox.style.display = "flex";
  
    const closeButton = document.querySelector(".custom-alert-close");
    closeButton.addEventListener("click", () => {
          alertBox.style.display = "none";
    });
  }
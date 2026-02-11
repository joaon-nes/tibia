var mybutton = document.getElementById("backToTop");

window.onscroll = function () {
    if (document.body.scrollTop > 300 || document.documentElement.scrollTop > 300) {
        mybutton.style.display = "block";
    } else {
        mybutton.style.display = "none";
    }
};
mybutton.onclick = function () {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
};
let clickedStar = 0;
unsetStars = function (force) {
    for (let i = 1; i <=5; i++){
        if (force !== true && i <= clickedStar) {continue;}
        let icon = document.getElementById("star" + i).nextElementSibling.getElementsByTagName("i")[0];
        icon.classList.replace("bi-star-fill", "bi-star");
    }
}
setStars = function (number) {
    unsetStars()
    for (let i = number; i > 0; i--) {
        let icon = document.getElementById("star" + i).nextElementSibling.getElementsByTagName("i")[0];
        icon.classList.replace("bi-star", "bi-star-fill");
    }
}
let stars = document.getElementsByClassName("star")
for(let i = 0; i < stars.length; i++){
    stars[i].parentElement.addEventListener('mouseover', () => {setStars(i+1);});
    stars[i].parentElement.addEventListener('click', () => {setStars(i+1); clickedStar=i+1});
    stars[i].parentElement.addEventListener('mouseout', unsetStars);
}
document.getElementById("reviewForm").addEventListener('reset', () => {unsetStars(true);})
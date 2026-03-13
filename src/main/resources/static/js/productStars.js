let ratingElem = document.getElementById("productRating");
let rating = Number(ratingElem.getAttribute("rating"));
ratingElem.innerHTML = "<i class='bi bi-star-fill'> </i>".repeat(rating-rating%1)
    +"<i class='bi bi-star-half'> </i>".repeat(rating%1 >= 0.5 ? 1 : 0)
    +"<i class='bi bi-star'> </i>".repeat(5-(rating-rating%1)-(rating%1).toFixed())
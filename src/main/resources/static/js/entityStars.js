let ratingElems = Array.from(document.getElementsByClassName("entity-rating")   );
ratingElems.forEach(elem => {
    let rating = Number(elem.getAttribute("rating"));
    elem.innerHTML = "<i class='bi bi-star-fill'> </i>".repeat(rating-rating%1)
        +"<i class='bi bi-star-half'> </i>".repeat(rating%1 >= 0.5 ? 1 : 0)
        +"<i class='bi bi-star'> </i>".repeat(5-(rating-rating%1)-(rating%1).toFixed())
})
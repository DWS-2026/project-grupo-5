let reviews = Array.from(document.getElementsByClassName("review-rating"));
reviews.forEach(review => {
    let pos = review.className.search("rating-");
    let num = Number(review.className.substring(pos).at(-1))
    review.innerHTML = '<i class="bi bi-star-fill"> </i>'.repeat(num).concat('<i class="bi bi-star"> </i>'.repeat(5-num))
})
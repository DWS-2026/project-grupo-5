document.getElementById("file-input").onchange = function(event) {
    const target = event.target || window.event.srcElement;
    const previewImages = document.getElementById("preview-grid");
    previewImages.innerHTML = "";
    Array.from(target.files).forEach((file) => {
        previewImages.innerHTML +=
        `<div class="col-6 col-sm-4 col-md-3 col-lg-2">
            <div class="preview-card">
                <img src=${URL.createObjectURL(file)}>
                <button class="btn-remove"><i class="bi bi-x-lg"></i></button>
            </div>
        </div>`;
    })
}
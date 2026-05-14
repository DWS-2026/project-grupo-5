document.getElementById("file-input").onchange = function(event) {
    const target = event.target || window.event.srcElement;
    const previewGrid = document.getElementById("preview-grid");
    previewGrid.innerHTML = "";
    Array.from(target.files).forEach((file) => {
        const isImage = file.type.startsWith("image/");
        const previewContent = isImage
            ? `<img src="${URL.createObjectURL(file)}" alt="Preview of ${file.name}">`
            : `<div class="file-preview non-image-file">
                    <i class="bi bi-file-earmark-text"></i>
                    <span>${file.name}</span>
               </div>`;

        previewGrid.innerHTML +=
        `<div class="col-6 col-sm-4 col-md-3 col-lg-2">
            <div class="preview-card">
                ${previewContent}
            </div>
        </div>`;
    })
}
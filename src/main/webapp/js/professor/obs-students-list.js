// Filter situation
const buttonStatusFilter = document.getElementById("buttonStatusFilter");
const popupFilter = document.getElementById("popupFilter");

buttonStatusFilter.addEventListener("click",(e)=>{

    e.stopPropagation();

    const rect = buttonStatusFilter.getBoundingClientRect();

    popupFilter.style.left = rect.left + "px";
    popupFilter.style.top = rect.bottom + window.scrollY + 10 + "px";

    popupFilter.classList.toggle("show");
});

document.addEventListener("click",()=>{
    popupFilter.classList.remove("show");
});

popupFilter.addEventListener("click",(e)=>{

    const item = e.target.closest(".popup-filter-item");

    if(!item) return;

    if(item.classList.contains("clear")){
        window.location = "selectClass";
        return;
    }

    const filter = item.dataset.filter;

    window.location = `selectClass?status=${filter}`;
});
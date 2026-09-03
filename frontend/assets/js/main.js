const menuButton = document.querySelector("#menu-button");
const mobileMenu = document.querySelector("#mobile-menu");
const currentYear = document.querySelector("#current-year");

function closeMobileMenu() {
    if (!menuButton || !mobileMenu) return;

    mobileMenu.classList.add("hidden");
    menuButton.setAttribute("aria-expanded", "false");
    menuButton.setAttribute("aria-label", "Abrir menú principal");
}

function toggleMobileMenu() {
    if (!menuButton || !mobileMenu) return;

    const menuIsOpen = menuButton.getAttribute("aria-expanded") === "true";
    mobileMenu.classList.toggle("hidden", menuIsOpen);
    menuButton.setAttribute("aria-expanded", String(!menuIsOpen));
    menuButton.setAttribute(
        "aria-label",
        menuIsOpen ? "Abrir menú principal" : "Cerrar menú principal"
    );
}

menuButton?.addEventListener("click", toggleMobileMenu);

mobileMenu?.addEventListener("click", (event) => {
    if (event.target.closest("a")) closeMobileMenu();
});

document.addEventListener("keydown", (event) => {
    if (event.key === "Escape") closeMobileMenu();
});

window.addEventListener("resize", () => {
    if (window.innerWidth >= 1024) closeMobileMenu();
});

if (currentYear) currentYear.textContent = new Date().getFullYear();

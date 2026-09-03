const buscarEspecialidad = document.getElementById("buscarEspecialidad");
const listaEspecialidades = document.getElementById("listaEspecialidades");

const especialidades = Array.from(listaEspecialidades.children);

buscarEspecialidad.addEventListener("input", () => {
    const texto = buscarEspecialidad.value.trim().toLowerCase();

    especialidades.forEach((especialidad) => {
        const nombre = especialidad.querySelector("h3").textContent.toLowerCase();

        if (nombre.includes(texto)) {
            especialidad.style.display = "";
        } else {
            especialidad.style.display = "none";
        }
    });
});
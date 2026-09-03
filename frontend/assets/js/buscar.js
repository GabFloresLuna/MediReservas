const buscarEspecialidad = document.getElementById("buscarEspecialidad");
const especialidades = document.querySelectorAll("#listaEspecialidades > div");

buscarEspecialidad.addEventListener("input", function () {
    const texto = this.value.toLowerCase().trim();

    especialidades.forEach(function (especialidad) {
        const nombre = especialidad.querySelector("h3").textContent.toLowerCase();

        if (nombre.includes(texto)) {
            especialidad.style.display = "block";
        } else {
            especialidad.style.display = "none";
        }
    });
});

const buscarMedico = document.getElementById("buscarMedico");
const filtroEspecialidad = document.getElementById("filtroEspecialidad");
const medicos = document.querySelectorAll("#listaMedicos .medico");

function filtrarMedicos() {
    const nombreBuscado = buscarMedico.value.toLowerCase().trim();
    const especialidadSeleccionada = filtroEspecialidad.value;

    medicos.forEach(function (medico) {
        const nombre = medico.querySelector("h3").textContent.toLowerCase();
        const especialidad = medico.querySelector("p").textContent
            .replace("Especialidad: ", "")
            .trim();

        const coincideNombre = nombre.includes(nombreBuscado);
        const coincideEspecialidad =
            especialidadSeleccionada === "" ||
            especialidad === especialidadSeleccionada;

        if (coincideNombre && coincideEspecialidad) {
            medico.style.display = "block";
        } else {
            medico.style.display = "none";
        }
    });
}

buscarMedico.addEventListener("input", filtrarMedicos);
filtroEspecialidad.addEventListener("change", filtrarMedicos);
const buscarEspecialidad = document.getElementById("buscarEspecialidad");
const especialidades = document.querySelectorAll("#listaEspecialidades > div");
const mensajeSinEspecialidades = document.getElementById("mensajeSinEspecialidades");

buscarEspecialidad.addEventListener("input", function () {
    const texto = this.value.toLowerCase().trim();
    let encontrados = 0;

    especialidades.forEach(function (especialidad) {
        const nombre = especialidad.querySelector("h3").textContent.toLowerCase();

        if (nombre.includes(texto)) {
            especialidad.style.display = "block";
            encontrados++;
        } else {
            especialidad.style.display = "none";
        }
    });

    if (encontrados === 0) {
        mensajeSinEspecialidades.style.display = "block";
    } else {
        mensajeSinEspecialidades.style.display = "none";
    }
});

const buscarMedico = document.getElementById("buscarMedico");
const filtroEspecialidad = document.getElementById("filtroEspecialidad");
const medicos = document.querySelectorAll("#listaMedicos .medico");
const mensajeSinMedicos = document.getElementById("mensajeSinMedicos");

function filtrarMedicos() {
    const nombreBuscado = buscarMedico.value.toLowerCase().trim();
    const especialidadSeleccionada = filtroEspecialidad.value;
    let encontrados = 0;

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
            encontrados++;
        } else {
            medico.style.display = "none";
        }
    });

    if (encontrados === 0) {
        mensajeSinMedicos.style.display = "block";
    } else {
        mensajeSinMedicos.style.display = "none";
    }
}

buscarMedico.addEventListener("input", filtrarMedicos);
filtroEspecialidad.addEventListener("change", filtrarMedicos);
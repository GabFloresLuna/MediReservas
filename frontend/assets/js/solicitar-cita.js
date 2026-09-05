const formulario = document.getElementById("formularioCita");
const mensajeSolicitud = document.getElementById("mensajeSolicitud");

const especialidad = document.getElementById("especialidad");
const medico = document.getElementById("medico");
const fecha = document.getElementById("fecha");
const hora = document.getElementById("hora");
const motivo = document.getElementById("motivo");

const errorEspecialidad = document.getElementById("errorEspecialidad");
const errorMedico = document.getElementById("errorMedico");
const errorFecha = document.getElementById("errorFecha");
const errorHora = document.getElementById("errorHora");
const errorMotivo = document.getElementById("errorMotivo");
const errorModalidad = document.getElementById("errorModalidad");

const medicosPorEspecialidad = {
    "Cardiología": ["Dr. Juan Pérez"],
    "Pediatría": ["Dra. Laura Gómez"],
    "Dermatología": ["Dr. Carlos Rodríguez"],
    "Neurología": ["Dra. Ana Martínez"],
    "Traumatología": ["Dr. Andrés López"],
    "Ginecología": ["Dra. María Torres"]
};

especialidad.addEventListener("change", function () {

    medico.innerHTML = '<option value="">Seleccione un médico</option>';

    if (this.value !== "") {

        medicosPorEspecialidad[this.value].forEach((nombre) => {

            const opcion = document.createElement("option");
            opcion.value = nombre;
            opcion.textContent = nombre;

            medico.appendChild(opcion);

        });

    }

});

formulario.addEventListener("submit", function (event) {

    event.preventDefault();

    errorEspecialidad.textContent = "";
    errorMedico.textContent = "";
    errorFecha.textContent = "";
    errorHora.textContent = "";
    errorMotivo.textContent = "";
    errorModalidad.textContent = "";

    mensajeSolicitud.classList.add("hidden");

    let formularioValido = true;

    if (especialidad.value === "") {
        errorEspecialidad.textContent = "Seleccione una especialidad.";
        formularioValido = false;
    }

    if (medico.value === "") {
        errorMedico.textContent = "Seleccione un médico.";
        formularioValido = false;
    }

    if (fecha.value === "") {
        errorFecha.textContent = "Seleccione una fecha.";
        formularioValido = false;
    }

    if (hora.value === "") {
        errorHora.textContent = "Seleccione una hora.";
        formularioValido = false;
    }

    if (motivo.value.trim() === "") {
        errorMotivo.textContent = "Ingrese el motivo de la consulta.";
        formularioValido = false;
    }

    const modalidad = document.querySelector('input[name="modalidad"]:checked');

    if (!modalidad) {
        errorModalidad.textContent = "Seleccione una modalidad.";
        formularioValido = false;
    }

    if (!formularioValido) {
        return;
    }

    mensajeSolicitud.classList.remove("hidden");

    formulario.reset();
    medico.innerHTML = '<option value="">Seleccione un médico</option>';

});
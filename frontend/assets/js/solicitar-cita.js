const formulario = document.getElementById("formularioCita");
const mensajeSolicitud = document.getElementById("mensajeSolicitud");

formulario.addEventListener("submit", function (event) {
    event.preventDefault();

    const especialidad = document.getElementById("especialidad").value;
    const medico = document.getElementById("medico").value;
    const fecha = document.getElementById("fecha").value;
    const hora = document.getElementById("hora").value;
    const motivo = document.getElementById("motivo").value.trim();
    const modalidad = document.querySelector('input[name="modalidad"]:checked');

    if (
        especialidad === "" ||
        medico === "" ||
        fecha === "" ||
        hora === "" ||
        motivo === "" ||
        modalidad === null
    ) {
        alert("Debe completar todos los campos del formulario.");
        return;
    }

    mensajeSolicitud.style.display = "block";
    formulario.reset();
});
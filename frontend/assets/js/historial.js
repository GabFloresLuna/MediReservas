document.addEventListener("DOMContentLoaded", function () {

    const antecedentes = document.querySelectorAll("#antecedentesMedicos > div");
    const alergias = document.querySelectorAll("#listaAlergias > div");
    const medicamentos = document.querySelectorAll("#listaMedicamentos > div");
    const consultas = document.querySelectorAll("#listaConsultas > div");

    const mensajeSinAntecedentes = document.getElementById("mensajeSinAntecedentes");
    const mensajeSinAlergias = document.getElementById("mensajeSinAlergias");
    const mensajeSinMedicamentos = document.getElementById("mensajeSinMedicamentos");
    const mensajeSinConsultas = document.getElementById("mensajeSinConsultas");

    if (antecedentes.length === 0) {
        mensajeSinAntecedentes.style.display = "block";
    }

    if (alergias.length === 0) {
        mensajeSinAlergias.style.display = "block";
    }

    if (medicamentos.length === 0) {
        mensajeSinMedicamentos.style.display = "block";
    }

    if (consultas.length === 0) {
        mensajeSinConsultas.style.display = "block";
    }

});

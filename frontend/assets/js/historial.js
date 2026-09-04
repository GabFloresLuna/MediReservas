const antecedentes = document.querySelectorAll("#antecedentesMedicos > div");
const alergias = document.querySelectorAll("#listaAlergias > div");
const medicamentos = document.querySelectorAll("#listaMedicamentos > div");
const consultas = document.querySelectorAll("#listaConsultas > div");

const mensajeSinAntecedentes = document.getElementById("mensajeSinAntecedentes");
const mensajeSinAlergias = document.getElementById("mensajeSinAlergias");
const mensajeSinMedicamentos = document.getElementById("mensajeSinMedicamentos");
const mensajeSinConsultas = document.getElementById("mensajeSinConsultas");

function verificarSeccion(lista, mensaje) {
    let visibles = 0;

    lista.forEach((elemento) => {
        if (elemento.style.display !== "none") {
            visibles++;
        }
    });

    if (visibles === 0) {
        mensaje.classList.remove("hidden");
    } else {
        mensaje.classList.add("hidden");
    }
}

verificarSeccion(antecedentes, mensajeSinAntecedentes);
verificarSeccion(alergias, mensajeSinAlergias);
verificarSeccion(medicamentos, mensajeSinMedicamentos);
verificarSeccion(consultas, mensajeSinConsultas);
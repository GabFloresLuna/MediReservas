const buscarCita = document.getElementById("buscarCita");
const estadoCita = document.getElementById("estadoCita");
const citas = document.querySelectorAll(".cita");
const mensajeSinCitas = document.getElementById("mensajeSinCitas");

function filtrarCitas() {

    const texto = buscarCita.value.toLowerCase();
    const estado = estadoCita.value;

    let visibles = 0;

    citas.forEach((cita) => {

        const medico = cita.querySelector("p").textContent.toLowerCase();
        const estadoActual = cita.querySelector(".estado").textContent;

        const coincideMedico = medico.includes(texto);
        const coincideEstado = estado === "" || estadoActual === estado;

        if (coincideMedico && coincideEstado) {
            cita.style.display = "block";
            visibles++;
        } else {
            cita.style.display = "none";
        }

    });

    if (visibles === 0) {
        mensajeSinCitas.style.display = "block";
    } else {
        mensajeSinCitas.style.display = "none";
    }

}

buscarCita.addEventListener("input", filtrarCitas);
estadoCita.addEventListener("change", filtrarCitas);

const botonesCancelar = document.querySelectorAll(".cancelarCita");

botonesCancelar.forEach((boton) => {

    boton.addEventListener("click", function () {

        if (boton.disabled) {
            return;
        }

        const cita = boton.parentElement;
        const estado = cita.querySelector(".estado");

        const confirmar = confirm("¿Desea cancelar esta cita médica?");

        if (confirmar) {
            estado.textContent = "Cancelada";
            boton.textContent = "Cancelada";
            boton.disabled = true;
        }

    });

});
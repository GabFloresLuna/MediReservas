document.addEventListener("DOMContentLoaded", function () {

    const formulario = document.getElementById("formularioContacto");

    const nombre = document.getElementById("nombre");
    const correo = document.getElementById("correo");
    const asunto = document.getElementById("asunto");
    const mensaje = document.getElementById("mensaje");

    const errorNombre = document.getElementById("errorNombre");
    const errorCorreo = document.getElementById("errorCorreo");
    const errorAsunto = document.getElementById("errorAsunto");
    const errorMensaje = document.getElementById("errorMensaje");

    const mensajeExito = document.getElementById("mensajeExito");


    formulario.addEventListener("submit", function (event) {

        event.preventDefault();

        let formularioValido = true;


        errorNombre.textContent = "";
        errorCorreo.textContent = "";
        errorAsunto.textContent = "";
        errorMensaje.textContent = "";

        mensajeExito.querySelector("p").textContent = "";


        if (nombre.value.trim() === "") {

            errorNombre.textContent =
                "El nombre es obligatorio.";

            formularioValido = false;
        }


        if (correo.value.trim() === "") {

            errorCorreo.textContent =
                "El correo electrónico es obligatorio.";

            formularioValido = false;

        } else {

            const formatoCorreo =
                /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

            if (!formatoCorreo.test(correo.value.trim())) {

                errorCorreo.textContent =
                    "Ingresa un correo electrónico válido.";

                formularioValido = false;
            }
        }


        if (asunto.value.trim() === "") {

            errorAsunto.textContent =
                "El asunto es obligatorio.";

            formularioValido = false;
        }


        if (mensaje.value.trim() === "") {

            errorMensaje.textContent =
                "El mensaje es obligatorio.";

            formularioValido = false;
        }


        if (formularioValido) {

            mensajeExito.querySelector("p").textContent =
                "El mensaje fue enviado correctamente.";

            formulario.reset();
        }

    });

});

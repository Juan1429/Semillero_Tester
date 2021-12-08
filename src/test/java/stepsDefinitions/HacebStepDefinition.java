package stepsDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import drivers.GoogleChromeDriver;
import steps.HacebSteps;

import java.io.IOException;

public class HacebStepDefinition {

    HacebSteps hacebSteps = new HacebSteps();

    @Given("^que me encuentre en la pagina de Haceb$")
    public void queMeEncuentreEnLaPaginaDeHaceb() {
        hacebSteps.abrirPagina();

    }


    @When("^busque los productos$")
    public void busqueLosProductos() throws IOException {
        hacebSteps.Busqueda_Validacion_Excel();

    }

    @Then("^podre ver en pantalla$")
    public void podreVerEnPantalla() {
        GoogleChromeDriver.driver.quit();

    }





}

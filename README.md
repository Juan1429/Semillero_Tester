##Reto#1
**¿Qué se hizo?**

Se realizó una prueba automatizada en la página de *https://www.haceb.com* . Esta prueba consiste en buscar, seleccionar y mostrar en pantalla cinco productos diferentes, utilizando POM, Cucumber y un archivo de Excel, el cual contiene los productos.

####¿Cómo se hizo?

**build.gradle**

El archivo *build.gradle* lo podemos encontrar en la raíz del proyecto, este archivo nos define configuraciones que se aplican a todos los módulos del proyecto.

```
apply plugin: 'java-library'
apply plugin: 'net.serenity-bdd.aggregator'
apply plugin: 'eclipse'

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()

}

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()

    }
    dependencies {
        classpath("net.serenity-bdd:serenity-gradle-plugin:2.0.80")
    }
}

dependencies {
    implementation 'net.serenity-bdd:serenity-junit:2.0.80'
    implementation 'net.serenity-bdd:serenity-cucumber:1.9.45'
    implementation 'net.serenity-bdd:serenity-core:2.0.80'
    implementation 'org.slf4j:slf4j-simple:1.7.7'
    implementation group: 'org.apache.poi', name: 'poi', version: '3.17'
    implementation group: 'org.apache.poi', name: 'poi-ooxml', version: '3.17'
}

test {
    ignoreFailures = true
}
gradle.startParameter.continueOnFailure = true
```

Para que estas configuraciones funcionen se debe actualizar en build.gradle.


**Paquete archivoexcel **


En este paquete creamos la clase llamada *ArchivoExcel*  la cual contiene el código Java necesario para leer nuestro archivo de Excel, y así poder validar los nombres de los productos que se van a buscar para la prueba.

```java
package archivoexcel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ArchivoExcel {

    public static ArrayList<Map<String, String>> leerDatosDeHojaDeExcel(String rutaDeExcel, String hojaDeExcel) throws IOException {
        ArrayList<Map<String, String>> arrayListDatoPlanTrabajo = new ArrayList<Map<String, String>>();
        Map<String, String> informacionProyecto = new HashMap<String, String>();
        File file = new File(rutaDeExcel);
        FileInputStream inputStream = new FileInputStream(file);
        XSSFWorkbook newWorkbook = new XSSFWorkbook(inputStream);
        XSSFSheet newSheet = newWorkbook.getSheet(hojaDeExcel);
        Iterator<Row> rowIterator = newSheet.iterator();
        Row titulos = rowIterator.next();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                cell.getColumnIndex();
                switch (cell.getCellTypeEnum()) {
                    case STRING:
                        informacionProyecto.put(titulos.getCell(cell.getColumnIndex()).toString(), cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        informacionProyecto.put(titulos.getCell(cell.getColumnIndex()).toString(), String.valueOf((long) cell.getNumericCellValue()));
                        break;
                    case BLANK:
                        informacionProyecto.put(titulos.getCell(cell.getColumnIndex()).toString(), "");
                        break;
                    default:
                }
            }
            arrayListDatoPlanTrabajo.add(informacionProyecto);
            informacionProyecto = new HashMap<String, String>();
        }
        return arrayListDatoPlanTrabajo;
    }



}

```




**Paquete drivers **

En el paquete de los drivers encontramos la clases llamada *GoogleChromeDirver* , esta clase nos ayuda a levantar el navegador en el cual relizaremos la búsqueda de la página a la que queremos probar de forma  automatizada.

```java
package drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class GoogleChromeDriver {

    public static WebDriver driver;

    public static void chromeWebDriver(String url){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-infobars");
        driver = new ChromeDriver(options);
        driver.get(url);
    }
}

```


**Paquete pages**

El paquete pages contiene la clase *HacebPages*  la cual nos ayuda a definir nuestros ***Xpath***, estos Xpath son la dirección de los elementos que se encuentran en la página, los cuales necesitamos para realizar la prueba; también encontraremos nuestros set y get, quienes nos ayudan a llevar y traer nuestros Xpath.

```java
package pages;

import org.openqa.selenium.By;

public class HacebPages {


    By txtBuscador = By.xpath("//input[@placeholder='¿Buscas un producto en especial?']");
    By btnBuscador = By.xpath("//button[@class='btn btn-search']");
    By btnElementoBusqueda;
    By txtElementoBusqueda;


    public By getTxtBuscador() {
        return txtBuscador;
    }

    public By getBtnBuscador() {
        return btnBuscador;
    }

    public By getBtnElementoBusqueda() {
        return btnElementoBusqueda;
    }

    public By getTxtElementoBusqueda() {
        return txtElementoBusqueda;
    }

    public void setBtnElementoBusqueda(String producto) {
        this.btnElementoBusqueda = By.xpath("//div[@class='shelve__item' and @data-name='"+producto+"']");
    }


    public void setTxtElementoBusqueda(String producto) {
        this.txtElementoBusqueda = By.xpath("//div[@class='column']//h1[contains(text(),'"+producto+"')]");
    }


}


```


**Paquete steps**

El paquete steps posee la clase *HacebSteps*, esta clase contiene los métodos que se van a ejecutar dentro del código para la automatización funcione de manera correcta.

```java
package steps;


import archivoexcel.ArchivoExcel;
import drivers.GoogleChromeDriver;
import org.junit.Assert;
import org.openqa.selenium.By;
import pages.HacebPages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public class HacebSteps {

    HacebPages hacebPages = new HacebPages();
    ArchivoExcel lecturaExcel = new ArchivoExcel();

    public void abrirPagina(){GoogleChromeDriver.chromeWebDriver("https://www.haceb.com/");}


    public void BuscarElementoEnHaceb (String producto){

        GoogleChromeDriver.driver.findElement(hacebPages.getTxtBuscador()).sendKeys(producto);
        GoogleChromeDriver.driver.findElement(hacebPages.getBtnBuscador()).click();
        hacebPages.setBtnElementoBusqueda(producto);
        GoogleChromeDriver.driver.findElement(hacebPages.getBtnElementoBusqueda()).click();

    }

    public void buscarElementoEnHacebs(String productos)  {
        try {
            escribirEnTexto(hacebPages.getTxtBuscador(), productos);
            Thread.sleep(1000);
            clicEnElemento(hacebPages.getBtnBuscador());
            Thread.sleep(1000);
            hacebPages.setBtnElementoBusqueda(productos);
            Thread.sleep(1000);
            clicEnElemento(hacebPages.getBtnElementoBusqueda());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void validarElementoEnPantalla(String producto){
        hacebPages.setTxtElementoBusqueda(producto);
        Assert.assertEquals(producto,GoogleChromeDriver.driver.findElement(hacebPages.getTxtElementoBusqueda()).getText());

    }

    public void Busqueda_Validacion_Excel () throws IOException {

        ArrayList<Map<String, String>> listaProductos;
        listaProductos = lecturaExcel.leerDatosDeHojaDeExcel("Libro1.xlsx","Hoja1");
        for (Map<String, String> datos: listaProductos){
            String producto = datos.get("Productos");
            buscarElementoEnHacebs(producto);
            validarElementoEnPantalla(producto);
        }
    }

    public void escribirEnTexto(By elemento, String texto) {GoogleChromeDriver.driver.findElement(elemento).sendKeys(texto);}

    public void clicEnElemento(By elemento) {GoogleChromeDriver.driver.findElement(elemento).click();}

}

```


**Paquete runners **

Este paquete tiene la clase *HacebRunners* , dicha clase nos ayuda a que el código ejecute los parámetros del Cucumber, el cual está compuesto por la clase que contiene el paquete de los stepsDefinitions y por el archivo que contiene el directorio llamado features.

```java
package runners;



import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src\\test\\resources\\features\\Haceb.feature",
        glue = "stepsDefinitions",
        snippets = SnippetType.CAMELCASE
)

public class HacebRunner {
}

```


**Paquete stepsDefinitions**

El paquete stepsDefinitions tiene la case *HacebStepDefinition*,  esta clase define el paso a paso de lo que se va a hacer dentro de la automatización en la página web con ayuda de la clase *HaceSteps*.

```java
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

```

**Directorio features**

El directorio features posee el archivo *Haceb.feature* , el cual contiene el lenguaje ordinario llamado **Gherkin**, este lenguaje nos ayuda a comprender el comportamiento de las pruebas en un idioma lógico para que se puede entender con mayor claridad.

```
Feature: HU-001 Buscador Haceb
  Yo como usuario de Haceb
  Quiero buscar 5 productos en la plataforma
  Para ver el nombre del producto en pantalla

  Scenario: Buscar producto
    Given que me encuentre en la pagina de Haceb
    When busque los productos
    Then podre ver en pantalla

```
**¿Cómo se ejecuta?**
Es necesario tener instalado Java para la ejecución del código.
















Feature: HU-001 Buscador Haceb
  Yo como usuario de Haceb
  Quiero buscar 5 productos en la plataforma
  Para ver el nombre del producto en pantalla

  Scenario: Buscar producto
    Given que me encuentre en la pagina de Haceb
    When busque los productos
    Then podre ver en pantalla

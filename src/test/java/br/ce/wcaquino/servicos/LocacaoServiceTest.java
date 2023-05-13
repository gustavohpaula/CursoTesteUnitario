package br.ce.wcaquino.servicos;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import org.hamcrest.CoreMatchers;
import org.junit.*;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class LocacaoServiceTest {

    private static LocacaoService service;
    private static int count = 0;
    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        System.out.println("Antes dos metodos");
        service = new LocacaoService();
        System.out.println(count);
        count = count + 1;
    }

    @After
    public void tearDown() {
        System.out.println("depois dos metodos");
    }

    @BeforeClass
    public static void setupClass() {
        System.out.println("Antes da classe");
        service = new LocacaoService();
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("depois da classes");
    }

    @Test
    public void deveAlugarFilme() throws Exception {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme1 = new Filme("Filme 1", 2, 5.0);
        Filme filme2 = new Filme("Filme 2", 3, 5.0);
        List<Filme> filmes = new ArrayList<>();
        filmes.add(filme1);
        filmes.add(filme2);

        //acao
        Locacao locacao = null;

        locacao = service.alugarFilme(usuario, filmes);

        Assert.assertEquals(10.0, locacao.getValor(), 0.01);
        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));

        error.checkThat(locacao.getValor(), is(10.0));
        error.checkThat(locacao.getValor(), is(not(6.0)));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterDataComDiferencaDias(1)), is(false));

        //verificacao

    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void naoDeveAlugarFilmeSemEstoque() throws Exception {

        Usuario usuario = new Usuario("Usuario 1");
        Filme filme1 = new Filme("Filme 1", 0, 5.0);
        Filme filme2 = new Filme("Filme 2", 3, 5.0);
        List<Filme> filmes = new ArrayList<>();
        filmes.add(filme1);
        filmes.add(filme2);
        //acao
        Locacao locacao = null;

        locacao = service.alugarFilme(usuario, filmes);

    }

    @Test
    public void naoDeveAlugarFilmeSemEstoque2() {

        Usuario usuario = new Usuario("Usuario1");
        Filme filme1 = new Filme("Filme 1", 0, 5.0);
        Filme filme2 = new Filme("Filme 2", 3, 5.0);
        List<Filme> filmes = new ArrayList<>();
        filmes.add(filme1);
        filmes.add(filme2);
        //acao
        Locacao locacao = null;

        try {
            locacao = service.alugarFilme(usuario, filmes);
            Assert.fail("Deveria lançar Exception");
        } catch (Exception e) {
            assertThat(e.getMessage(), is("Filme sem estoque"));
        }

    }

    @Test
    public void naoDeveAlugarFilmeSemEstoque3() throws Exception {

        Usuario usuario = new Usuario("Usuario 1");
        Filme filme1 = new Filme("Filme 1", 0, 5.0);
        Filme filme2 = new Filme("Filme 2", 3, 5.0);
        List<Filme> filmes = new ArrayList<>();
        filmes.add(filme1);
        filmes.add(filme2);
        //acao
        Locacao locacao = null;

        exception.expect(Exception.class);
        exception.expectMessage("Filme sem estoque");

        locacao = service.alugarFilme(usuario, filmes);


    }

    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {

        Filme filme1 = new Filme("Filme 1", 2, 5.0);
        Filme filme2 = new Filme("Filme 2", 3, 5.0);
        List<Filme> filmes = new ArrayList<>();
        filmes.add(filme1);
        filmes.add(filme2);
        //acao
        try {
            service.alugarFilme(null, filmes);
            Assert.fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Sem Usuario"));
        }

    }

    @Test
    public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = new Usuario("Usuario 1");

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");
        service.alugarFilme(usuario, null);

    }

    @Test
    public void devePagar75PctNoFilmeTres() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme1 = new Filme("Filme 1", 2, 4.0);
        Filme filme2 = new Filme("Filme 2", 3, 4.0);
        Filme filme3 = new Filme("Filme 3", 3, 4.0);
        List<Filme> filmes = new ArrayList<>();
        filmes.add(filme1);
        filmes.add(filme2);
        filmes.add(filme3);

        Locacao resultado = service.alugarFilme(usuario, filmes);

        assertThat(resultado.getValor(), is(11.0));
    }

    @Test
    public void devePagar50PctNoFilmeQuatro() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme1 = new Filme("Filme 1", 2, 4.0);
        Filme filme2 = new Filme("Filme 2", 3, 4.0);
        Filme filme3 = new Filme("Filme 3", 3, 4.0);
        Filme filme4 = new Filme("Filme 4", 3, 4.0);
        List<Filme> filmes = new ArrayList<>();
        filmes.add(filme1);
        filmes.add(filme2);
        filmes.add(filme3);
        filmes.add(filme4);

        Locacao resultado = service.alugarFilme(usuario, filmes);

        assertThat(resultado.getValor(), is(13.0));
    }

    @Test
    public void devePagar25PctNoFilmeCinco() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme1 = new Filme("Filme 1", 2, 4.0);
        Filme filme2 = new Filme("Filme 2", 3, 4.0);
        Filme filme3 = new Filme("Filme 3", 3, 4.0);
        Filme filme4 = new Filme("Filme 4", 3, 4.0);
        Filme filme5 = new Filme("Filme 5", 3, 4.0);
        List<Filme> filmes = new ArrayList<>();
        filmes.add(filme1);
        filmes.add(filme2);
        filmes.add(filme3);
        filmes.add(filme4);
        filmes.add(filme5);

        Locacao resultado = service.alugarFilme(usuario, filmes);

        assertThat(resultado.getValor(), is(14.0));
    }

    @Test
    public void devePagar0PctNoFilmeSeis() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme1 = new Filme("Filme 1", 2, 4.0);
        Filme filme2 = new Filme("Filme 2", 3, 4.0);
        Filme filme3 = new Filme("Filme 3", 3, 4.0);
        Filme filme4 = new Filme("Filme 4", 3, 4.0);
        Filme filme5 = new Filme("Filme 5", 3, 4.0);
        Filme filme6 = new Filme("Filme 6", 3, 4.0);
        List<Filme> filmes = new ArrayList<>();
        filmes.add(filme1);
        filmes.add(filme2);
        filmes.add(filme3);
        filmes.add(filme4);
        filmes.add(filme5);
        filmes.add(filme6);

        Locacao resultado = service.alugarFilme(usuario, filmes);

        assertThat(resultado.getValor(), is(14.0));
    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme1 = new Filme("Filme 1", 2, 4.0);
        List<Filme> filmes = new ArrayList<>();
        filmes.add(filme1);

        Locacao retorno = service.alugarFilme(usuario, filmes);

        boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
        Assert.assertTrue(ehSegunda);

    }
}

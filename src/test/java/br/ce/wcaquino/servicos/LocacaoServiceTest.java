package br.ce.wcaquino.servicos;


import java.util.ArrayList;
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
    public void teste() throws Exception {
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
    public void testLocacao_filmeSemEstoque() throws Exception {

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
    public void testLocacao_filmeSemEstoque2() {

        Usuario usuario = new Usuario("Usuario 1");
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
    public void testLocacao_filmeSemEstoque3() throws Exception {

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
    public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException {

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
    public void testLocacao_filmeVazio() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = new Usuario("Usuario 1");

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");
        service.alugarFilme(usuario, null);

    }
}

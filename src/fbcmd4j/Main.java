package fbcmd4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Post;
import facebook4j.ResponseList;

public class Main {
	static final Logger logger = LogManager.getLogger(Main.class);

	private static final String CONFIG_DIR = "config";
	private static final String CONFIG_FILE = "fbcmd4j.properties";
	private static String seleccion = "";

	public static void main(String[] args)
	{
		logger.info("Iniciando app");
		Facebook fb =  null;
		Properties props = null;

		try
		{
			props = utils.loadConfigFile(CONFIG_DIR, CONFIG_FILE);
		} catch (IOException ex)
		{
			logger.error(ex);
		}

		try
		{
			Scanner scanner = new Scanner(System.in);
			while(true)
			{
				fb = utils.configFacebook(props);
				//System.out.println(fb);
				System.out.println("Cliente Facebook\n");
				System.out.println("1.- NewsFeed");
				System.out.println("2.- Wall");
				System.out.println("3.- Enviar Status");
				System.out.println("4.- Enviar Url");
				System.out.println("5.- Salir");
				System.out.println("Seleccione una opción:");

				try {
					seleccion= scanner.nextLine();
					switch (seleccion)
					{
					case "1":
						System.out.println("-NewsFeed");
						try{
							ResponseList<Post> newsFeed = fb.getFeed();
							System.out.println("Posts: " + newsFeed.size());
							for (Post p : newsFeed)
							{
								System.out.println("Post: " + p);
								//utils.printPost(p);
							}
							Save_Facebook("NewsFeed", newsFeed,scanner);
						}catch(FacebookException ex){
							System.out.println("Error al consultar el Feed: " + ex);
						}
						break;
					case "2":
						System.out.println("-Wall");
						ResponseList<Post> wall = fb.getPosts();
						for (Post p : wall)
						{
							utils.printPost(p);
						}
						 Save_Facebook("Wall", wall, scanner);
						break;
					case "3":
						System.out.println("-Enviar Status");
						System.out.print("\n-Indique el status: ");
						String estado = scanner.nextLine();
						utils.postStatus(estado, fb);
						break;
					case "4":
						System.out.println("Enviar Url");
						System.out.print("\n-Indique el URL: ");
						String link = scanner.nextLine();
						utils.postLink(link, fb);
						break;
					case "5":
						System.out.println("Saliendo");
						System.exit(0);
						break;
					default:
						logger.error("Opción no valida");
						break;
					}
				}
				catch (InputMismatchException e)
				{
					logger.error("Opción no valida. %s. \n", e.getClass());
				}
				catch (FacebookException e)
				{
					logger.error(e.getErrorMessage());
				}
				catch (Exception e)
				{
					logger.error(e);
				}
				System.out.println();
			}
		}
		catch (Exception ex) {
			logger.error(ex);
		}
	}


	public static void Save_Facebook(String fileName, ResponseList<Post> posts, Scanner scanner)
	{
		System.out.println("\n¿Quieres guardar el resultado en un archivo txt?(S/N)");
		String seleccion= scanner.nextLine();
		if ("s".contains(seleccion.toLowerCase()))
		{	List<Post> post = new ArrayList<>();
			try
			{
				for(int i = 0; i < posts.size(); i++)
				{
					post.add(posts.get(i));
				}
			}catch(NumberFormatException e)
			{
				logger.error(e);
			}
			//utils.Save_Post(fileName, post);
		}
	}
}

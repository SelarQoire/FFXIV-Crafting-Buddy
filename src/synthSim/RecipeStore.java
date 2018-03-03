package synthSim;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import crafting.CrafterClass;
import crafting.Element;

public class RecipeStore
{
	private final ArrayList<RecipeData> m_recipes;

	public RecipeStore()
	{
		m_recipes = new ArrayList<>();

		loadRecipes();
	}

	public RecipeData getRecipe(final int p_index)
	{
		return m_recipes.get(p_index);
	}

	private void loadJSONFile(final File p_file, final CrafterClass p_class)
	{
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(p_file);
			final JSONTokener jt = new JSONTokener(fis);
			jt.nextValue();// encoding marker in file - ignore
			final JSONArray jArr = (JSONArray)jt.nextValue();

			for(int i = 0; i < jArr.length(); i++)
			{
				final JSONObject arrObject = (JSONObject)jArr.get(i);
				loadOneRecipe(arrObject, p_class);
			}
		}
		catch(final FileNotFoundException | JSONException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				fis.close();
			}
			catch(final IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void loadOneRecipe(final JSONObject p_jo, final CrafterClass p_class) throws JSONException
	{
		int difficulty = 0;
		int maxQuality = 0;
		int effectiveLevel = 0;
		int nominalLevel = 0;
		int durability = 0;
		Element aspect = Element.NONE;

		@SuppressWarnings("rawtypes") final Iterator keyIterator = p_jo.keys();
		while(keyIterator.hasNext())
		{
			final String key = (String)keyIterator.next();
			if(key.equals("difficulty"))
			{
				difficulty = p_jo.getInt(key);
			}
			else if(key.equals("maxQuality"))
			{
				maxQuality = p_jo.getInt(key);
			}
			else if(key.equals("level"))
			{
				effectiveLevel = p_jo.getInt(key);
			}
			else if(key.equals("durability"))
			{
				durability = p_jo.getInt(key);
			}
			else if(key.equals("name"))
			{
				// name = p_jo.getJSONObject(key).getString("en");
			}
			else if(key.equals("baseLevel"))
			{
				nominalLevel = p_jo.getInt(key);
			}
			else if(key.equals("aspect"))
			{
				aspect = Element.getElement(p_jo.getString(key));
			}
			else if(key.equals("stars"))
			{
				// stars = p_jo.getInt(key);
			}
			else
			{
				throw new RuntimeException("unexpected JSON key");
			}
		}

		final RecipeData rd = new RecipeData(p_class, nominalLevel, effectiveLevel, difficulty, durability, maxQuality,
				aspect);
		m_recipes.add(rd);
	}

	private void loadRecipes()
	{
		final File dataDir = new File("data");
		for(final File file: dataDir.listFiles())
		{
			final String fName = file.getName();
			if(fName.endsWith(".json"))
			{
				final String className = fName.substring(0, fName.lastIndexOf("."));
				final CrafterClass cClass = CrafterClass.getClass(className);
				loadJSONFile(file, cClass);
			}
		}
	}

	public int numRecipes()
	{
		return m_recipes.size();
	}
}

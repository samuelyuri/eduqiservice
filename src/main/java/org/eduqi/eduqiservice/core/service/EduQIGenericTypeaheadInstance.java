package org.eduqi.eduqiservice.core.service;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cleo.search.Element;
import cleo.search.Indexer;
import cleo.search.MultiIndexer;
import cleo.search.selector.ScoredElementSelectorFactory;
import cleo.search.store.ArrayStoreElement;
import cleo.search.store.MultiArrayStoreElement;
import cleo.search.tool.GenericTypeaheadInitializer;
import cleo.search.typeahead.GenericTypeahead;
import cleo.search.typeahead.GenericTypeaheadConfig;
import cleo.search.typeahead.MultiTypeahead;
import cleo.search.typeahead.Typeahead;
import cleo.search.typeahead.TypeaheadConfigFactory;

public class EduQIGenericTypeaheadInstance<E extends Element> implements EduQITypeaheadInstance<E> {
	
	private static final Logger LOG = Logger.getLogger(EduQIGenericTypeaheadInstance.class);
	
	final Indexer<E> indexer;
	final Typeahead<E> searcher;
	final ArrayStoreElement<E> elementStore;

	public EduQIGenericTypeaheadInstance(String name, File configPath) throws Exception {
		File[] configFiles = configPath.listFiles(new FileFilter() {
			public boolean accept(File path) {
				return path.getName().endsWith(".config");
			}
		});

		List<Indexer<E>> indexerList = new ArrayList<Indexer<E>>();
		List<Typeahead<E>> searcherList = new ArrayList<Typeahead<E>>();
		List<ArrayStoreElement<E>> storeList = new ArrayList<ArrayStoreElement<E>>();

		for(File configFile : configFiles) {
			LOG.info("---- Typeahead path: "+configFile.getPath());
			GenericTypeahead<E> gta = createTypeahead(configFile);
			indexerList.add(gta);
			searcherList.add(gta);
			storeList.add(gta.getElementStore());
		}

		// Create indexer, searcher and elementStore
		indexer = new MultiIndexer<E>(name, indexerList);
		searcher = new MultiTypeahead<E>(name, searcherList);
		elementStore = new MultiArrayStoreElement<E>(storeList);

		// Flush indexes upon shutdown
		addShutdownHook();
	}

	protected void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					indexer.flush();
				} catch (IOException e) {
					LOG.error(e,e);
				}
			}
		});
	}

	protected GenericTypeahead<E> createTypeahead(File configFile) throws Exception {
		// Create typeahead config
		GenericTypeaheadConfig<E> config =
				TypeaheadConfigFactory.createGenericTypeaheadConfig(configFile);
		config.setSelectorFactory(new ScoredElementSelectorFactory<E>());

		// Create typeahead initializer
		GenericTypeaheadInitializer<E> initializer = new GenericTypeaheadInitializer<E>(config);

		return (GenericTypeahead<E>)initializer.getTypeahead();
	}

	public final Indexer<E> getIndexer() {
		return indexer;
	}

	public final Typeahead<E> getSearcher() {
		return searcher;
	}

	public final ArrayStoreElement<E> getElementStore() {
		return elementStore;
	}
}

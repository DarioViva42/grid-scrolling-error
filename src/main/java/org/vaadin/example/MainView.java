package org.vaadin.example;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends SplitLayout {

	public MainView(FakeService service) {
		setOrientation(Orientation.VERTICAL);

		Grid<Model> grid = new Grid<>();

		grid.addColumn(Model::name).setHeader("Full name").setSortProperty("name").setSortable(true);
		grid.addColumn(Model::pokemon).setHeader("Favorite pokemon").setSortProperty("pokemon").setSortable(true);
		grid.addColumn(Model::animal).setHeader("Favourite animal").setSortProperty("animal").setSortable(true);

		grid.setMultiSort(true);

		CallbackDataProvider<Model, String> dataProvider = new CallbackDataProvider<>(
				query -> service.fetch(query.getOffset(), query.getLimit()).stream(),
				query -> service.count()
		);
		grid.setItems(dataProvider.withConfigurableFilter());

		grid.setColumnReorderingAllowed(true);
		grid.setPageSize(50);
		GridSingleSelectionModel<Model> selectionModel = (GridSingleSelectionModel<Model>) grid.setSelectionMode(Grid.SelectionMode.SINGLE);
		selectionModel.setDeselectAllowed(false);
		grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
		grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		grid.addThemeVariants(GridVariant.LUMO_COMPACT);

		grid.setSizeFull();

		Grid<Model> detailGrid = new Grid<>();
		detailGrid.setSelectionMode(Grid.SelectionMode.NONE);

		grid.setPageSize(50);

		detailGrid.addColumn(Model::name).setHeader("Full name");
		detailGrid.addColumn(Model::pokemon).setHeader("Favorite pokemon");
		detailGrid.addColumn(Model::animal).setHeader("Favourite animal");

		CallbackDataProvider<Model, String> detailProvider = new CallbackDataProvider<>(
				query -> service.fetch(query.getOffset(), query.getLimit()).stream(),
				query -> service.count()
		);
		ConfigurableFilterDataProvider<Model, Void, String> detailFilterProvider = detailProvider.withConfigurableFilter();
		detailGrid.setItems(detailFilterProvider);

		detailGrid.setSizeFull();

		setSizeFull();

		grid.addSelectionListener(selectionEvent -> {
			if (selectionEvent.getFirstSelectedItem().isPresent()) {
				detailGrid.setItems(detailFilterProvider);
				addToSecondary(detailGrid);
			}
		});

		addToPrimary(grid);
	}
}

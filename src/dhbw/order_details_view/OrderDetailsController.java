package dhbw.order_details_view;

import dhbw.datamodel.OrderModel;
import dhbw.datamodel.OrderedItemModel;
import dhbw.sa.kassensystem_rest.database.databaseservice.DatabaseService;
import dhbw.sa.kassensystem_rest.database.entity.Item;
import dhbw.sa.kassensystem_rest.database.entity.OrderedItem;
import dhbw.sa.kassensystem_rest.database.entity.Waiter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrderDetailsController implements Initializable
{
	DatabaseService databaseService;

	public TableView OrderedItemsTable;

	public Label orderIdLabel;
	public Label orderPriceLabel;
	public Label orderTableLabel;
	public Label orderDateLabel;
	public Label orderWaiterLabel;

	public TableColumn itemNameColumn;
	public TableColumn itemPriceColumn;
	public TableColumn itemProducedColumn;
	public TableColumn itemPaidColumn;
	public TableColumn itemCommentColumn;

	public void initialize(OrderModel order, DatabaseService databaseService)
	{
		orderIdLabel.setText(String.valueOf(order.getOrderID()));
		orderPriceLabel.setText(String.valueOf(order.getPrice()) + " €");
		orderTableLabel.setText(order.getTable());
		orderDateLabel.setText(order.getDate());
		Waiter waiter = databaseService.getWaiterByID(order.getWaiterID());
		orderWaiterLabel.setText(waiter.getWaiterID() + " - " +
				waiter.getPrename() + " " + waiter.getLastname());

		ArrayList<OrderedItem> orderedItems = databaseService.getOrderedItemsByOrderId(order.getOrderID());
		ObservableList<OrderedItemModel> orderedItemData = FXCollections.observableArrayList();
		// Collect data for OrderdItemModels shown in List
		for(OrderedItem o: orderedItems)
		{
			Item item = databaseService.getItemById(o.getItemID());
			String name = item.getName();
			double price = item.getRetailprice();
			boolean paid = o.isItemPaid();
			boolean produced = o.isItemProduced();
			String comment = o.getComment();
			orderedItemData.add(new OrderedItemModel(name, price, paid, produced, comment));
		}
		itemNameColumn.setCellValueFactory(new PropertyValueFactory<OrderModel, String>("name"));
		itemPriceColumn.setCellValueFactory(new PropertyValueFactory<OrderedItemModel, Double>("price"));
		itemPaidColumn.setCellValueFactory(new PropertyValueFactory<OrderedItemModel, Boolean>("paid"));
		itemProducedColumn.setCellValueFactory(new PropertyValueFactory<OrderedItemModel, Boolean>("produced"));
		itemCommentColumn.setCellValueFactory(new PropertyValueFactory<OrderedItemModel, String>("comment"));

		OrderedItemsTable.setItems(orderedItemData);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{

	}

}

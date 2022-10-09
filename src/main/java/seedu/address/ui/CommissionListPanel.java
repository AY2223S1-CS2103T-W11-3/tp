package seedu.address.ui;

import java.util.logging.Logger;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.commission.Commission;

/**
 * Panel containing the list of commissions.
 */
public class CommissionListPanel extends UiPart<Region> {
    private static final String FXML = "CommissionListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(CommissionListPanel.class);

    @FXML
    private ListView<Commission> commissionListView;

    /**
     * Creates a {@code CommissionListPanel} with the given {@code ObservableList}.
     */
    public CommissionListPanel(ObservableValue<FilteredList<Commission>> observableCommissionList) {
        super(FXML);
        this.updateUI(observableCommissionList.getValue());

        observableCommissionList.addListener((observable, oldValue, newValue) -> {
                this.updateUI(newValue);
            }
        );
    }


    private void updateUI(ObservableList<Commission> commissionList) {
        commissionListView.setItems(commissionList);
        commissionListView.setCellFactory(listView -> new CommissionListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Commission} using a {@code CommissionCard}.
     */
    class CommissionListViewCell extends ListCell<Commission> {
        @Override
        protected void updateItem(Commission commission, boolean empty) {
            super.updateItem(commission, empty);

            if (empty || commission == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new CommissionCard(commission, getIndex() + 1).getRoot());
            }
        }
    }

}

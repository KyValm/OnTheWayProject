import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class ExamplePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGetPriorityItems', 'onGetAllItems', 'renderItems', 'onGetItemById','onGetItemsByCategory'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    mount() {
        document.getElementById('all-priority-items').addEventListener('click', this.onGetPriorityItems);
        document.getElementById('all-items').addEventListener('click', this.onGetAllItems);
        document.getElementById('get-by-id').addEventListener('click', this.onGetItemById);
        // document.getElementById('getItemsByCategory').addEventListener('click', this.onGetItemsByCategory);
        this.client = new ExampleClient();
        this.dataStore.addChangeListener(this.renderItems);
        this.renderItems();
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderItems() {

        const mode = this.dataStore.get("eventHandler");

        let itemsHtml = "";

        if (mode === 1) {
            const items = this.dataStore.get("priorityItems");

            if (items) {
                for(const item of items) {
                    itemsHtml += `               
                    <div class="grid-item" id="${item.itemId}">
                        <td>${item.itemId}</td>
                        <td>${item.description}</td>
                        <td>${item.currentQty}</td>
                        <td>${item.reorderQty}</td>
                        <td>${item.qtyTrigger}</td>
                        <td>${item.orderDate}</td>
<!--                        <td><input class="btn" id="update-arrow" type="button" value="Update" onclick="openUpdateMessageForm(this)"></td>-->
                    </div>
                `;
                }
                this.showMessage(`Got priority items list!`);

            }  else if (mode === 2) {

                const items = this.dataStore.get("items");

                if (items) {
                    for (const item of items) {
                        itemsHtml += `                                              
                    <tr class="grid-item" id="${item.itemId}">
                        <td>${item.itemId}</td>
                        <td>${item.description}</td>
                        <td>${item.currentQty}</td>                     
                        <td>${item.reorderQty}</td>
                        <td>${item.qtyTrigger}</td>
                        <td>${item.orderDate}</td>                                      
                        <td><input class="btn" id="update-arrow" type="button" value="Update" onclick="openUpdateMessageForm(this)"></td>
                    </tr>                            
                `;
                    }
                }
            }
            document.getElementById("allItemsToAdd").innerHTML += itemsHtml;
        }
    }

    //Event handlers -------------------------------------------------------------------------------------------------

     async onGetPriorityItems() {
         const priorityItems = await this.client.getPriorityList(this.errorHandler);
         // if (priorityItems && priorityItems.length > 0) {
         //     for (const item of priorityItems) {
         //         item.item = this.fetchItem(item.itemId);
         //     }
         // }
         this.dataStore.set("eventHandler", 1);
         this.dataStore.set("priorityItems", priorityItems);
     }

    async onGetAllItems() {
        const allItems = await this.client.getAllInventoryItems(this.errorHandler);
        this.dataStore.set("eventHandler", 2);
        this.dataStore.set("items", allItems);
    }
    // finish endpoints and finish formatting

    async onGetItemById() {

    }

    async onGetItemsByCategory() {

    }

    async fetchItem(itemId) {
        return await this.client.getItemById(itemId, this.errorHandler);
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const examplePage = new ExamplePage();
    examplePage.mount();
};

window.addEventListener('DOMContentLoaded', main);

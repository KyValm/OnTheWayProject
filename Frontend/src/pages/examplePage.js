import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ItemClient from "../api/itemClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class ExamplePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGetPriorityItems'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        document.getElementById('all-priority-items').addEventListener('click', this.onGetPriorityItems);
        this.client = new ItemClient();
        this.dataStore.addChangeListener(this.renderExample);
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    // async onGetPriorityItems() {
    //     let itemsHtml = "";
    //
    //     const items = this.client.getPriorityList(this.errorHandler);
    //
    //     if(items) {
    //         for (const item of items) {
    //             itemsHtml += `
    //                 <tr class="card" id="${item.itemId}">
    //                     <td>${item.itemId}</td>
    //                     <td>${item.description}</td>
    //                     <td>${item.currentQty}</td>
    //                     <td>${item.reorderQty}</td>
    //                     <td>${item.qtyTrigger}</td>
    //                     <td>${item.orderDate}</td>
    //                     <td><input class="btn" id="update-arrow" type="button" value="Update" onclick="openUpdateMessageForm(this)"></td>
    //                 </tr>
    //             `;
    //         }
    //     }
    //     document.getElementById("allItemsToAdd").innerHTML += itemsHtml;
    // }

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

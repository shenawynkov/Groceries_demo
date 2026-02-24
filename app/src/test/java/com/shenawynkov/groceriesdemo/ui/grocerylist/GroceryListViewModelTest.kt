package com.shenawynkov.groceriesdemo.ui.grocerylist

import app.cash.turbine.test
import com.shenawynkov.groceriesdemo.R
import com.shenawynkov.groceriesdemo.domain.model.Category
import com.shenawynkov.groceriesdemo.domain.repository.FakeGroceryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GroceryListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: GroceryListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = GroceryListViewModel(FakeGroceryRepository())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `add item shows in list`() = runTest {
        viewModel.state.test {
            awaitItem() // initial empty

            viewModel.onIntent(GroceryListIntent.UpdateName("Apples"))
            viewModel.onIntent(GroceryListIntent.UpdateCategory(Category.FRUITS))
            viewModel.onIntent(GroceryListIntent.AddItem)

            val state = expectMostRecentItem()
            assertEquals(1, state.list.items.size)
            assertEquals("Apples", state.list.items[0].name)
            assertEquals(Category.FRUITS, state.list.items[0].category)
            assertEquals("", state.form.inputName)
        }
    }

    @Test
    fun `add blank item shows error`() = runTest {
        viewModel.state.test {
            awaitItem()

            viewModel.onIntent(GroceryListIntent.UpdateName("   "))
            viewModel.onIntent(GroceryListIntent.AddItem)

            val state = expectMostRecentItem()
            assertTrue(state.list.items.isEmpty())
            assertEquals(R.string.error_item_name_empty, state.form.validationErrorResId)
        }
    }

    @Test
    fun `toggle purchased flips status`() = runTest {
        viewModel.state.test {
            awaitItem()

            viewModel.onIntent(GroceryListIntent.UpdateName("Milk"))
            viewModel.onIntent(GroceryListIntent.AddItem)

            val afterAdd = expectMostRecentItem()
            val item = afterAdd.list.items[0]
            assertEquals(false, item.isPurchased)

            viewModel.onIntent(GroceryListIntent.TogglePurchased(item))

            val afterToggle = expectMostRecentItem()
            assertEquals(true, afterToggle.list.items[0].isPurchased)
        }
    }

    @Test
    fun `delete item removes it from list`() = runTest {
        viewModel.state.test {
            awaitItem()

            viewModel.onIntent(GroceryListIntent.UpdateName("Bread"))
            viewModel.onIntent(GroceryListIntent.AddItem)

            val afterAdd = expectMostRecentItem()
            assertEquals(1, afterAdd.list.items.size)

            viewModel.onIntent(GroceryListIntent.DeleteItem(afterAdd.list.items[0]))

            val afterDelete = expectMostRecentItem()
            assertTrue(afterDelete.list.items.isEmpty())
        }
    }

    @Test
    fun `edit item updates name and category`() = runTest {
        viewModel.state.test {
            awaitItem()

            viewModel.onIntent(GroceryListIntent.UpdateName("Chicken"))
            viewModel.onIntent(GroceryListIntent.UpdateCategory(Category.MEATS))
            viewModel.onIntent(GroceryListIntent.AddItem)

            val afterAdd = expectMostRecentItem()
            val item = afterAdd.list.items[0]

            viewModel.onIntent(GroceryListIntent.StartEdit(item))
            viewModel.onIntent(GroceryListIntent.ConfirmEdit("Steak", Category.MEATS))

            val afterEdit = expectMostRecentItem()
            assertEquals("Steak", afterEdit.list.items[0].name)
            assertNull(afterEdit.edit.editingItem)
        }
    }

    @Test
    fun `filter by category shows only matching items`() = runTest {
        viewModel.state.test {
            awaitItem()

            viewModel.onIntent(GroceryListIntent.UpdateName("Banana"))
            viewModel.onIntent(GroceryListIntent.UpdateCategory(Category.FRUITS))
            viewModel.onIntent(GroceryListIntent.AddItem)

            viewModel.onIntent(GroceryListIntent.UpdateName("Whole Wheat"))
            viewModel.onIntent(GroceryListIntent.UpdateCategory(Category.BREADS))
            viewModel.onIntent(GroceryListIntent.AddItem)

            val beforeFilter = expectMostRecentItem()
            assertEquals(2, beforeFilter.list.items.size)

            viewModel.onIntent(GroceryListIntent.SetFilter(Category.FRUITS))

            val afterFilter = expectMostRecentItem()
            assertEquals(1, afterFilter.list.items.size)
            assertEquals("Banana", afterFilter.list.items[0].name)
        }
    }

    @Test
    fun `sort alphabetical orders items by name`() = runTest {
        viewModel.state.test {
            awaitItem()

            viewModel.onIntent(GroceryListIntent.UpdateName("Zucchini"))
            viewModel.onIntent(GroceryListIntent.AddItem)
            viewModel.onIntent(GroceryListIntent.UpdateName("Apple"))
            viewModel.onIntent(GroceryListIntent.AddItem)

            viewModel.onIntent(GroceryListIntent.SetSort(SortOption.ALPHABETICAL))

            val sorted = expectMostRecentItem()
            assertEquals("Apple", sorted.list.items[0].name)
            assertEquals("Zucchini", sorted.list.items[1].name)
        }
    }

    @Test
    fun `sort by status puts unpurchased first`() = runTest {
        viewModel.state.test {
            awaitItem()

            viewModel.onIntent(GroceryListIntent.UpdateName("Milk"))
            viewModel.onIntent(GroceryListIntent.AddItem)
            viewModel.onIntent(GroceryListIntent.UpdateName("Eggs"))
            viewModel.onIntent(GroceryListIntent.AddItem)

            val afterAdd = expectMostRecentItem()
            viewModel.onIntent(GroceryListIntent.TogglePurchased(afterAdd.list.items[0])) // mark Milk purchased

            viewModel.onIntent(GroceryListIntent.SetSort(SortOption.BY_STATUS))

            val sorted = expectMostRecentItem()
            assertEquals("Eggs", sorted.list.items[0].name)
            assertEquals("Milk", sorted.list.items[1].name)
        }
    }
}

package com.android.flickphoto.ui.li


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.android.flickphoto.R
import com.android.flickphoto.databinding.FragmentPhotoListBinding
import com.android.flickphoto.models.Photo
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */

class PhotoListFragment : Fragment() {
    companion object {
        private const val TAG = "PhotoListFragment"
    }




    private lateinit var navController: NavController


    private val photoListViewModel: PhotoListViewModel by viewModel()
    private lateinit var binding: FragmentPhotoListBinding


    private val displayPhoto: (Photo) -> Unit = { photo ->
        val action = PhotoListFragmentDirections.actionPhotoListFragmentToDisplayPhotoFragment(photo)
        navController.navigate(action)
    }

    private var callbacks:Callbacks? =null
    interface Callbacks{
        fun onPhotoListFragmentDisplayed()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Log.d(TAG, "onCreate: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_photo_list, container, false)
        binding.viewModel = photoListViewModel
        binding.setLifecycleOwner(this.viewLifecycleOwner)
        binding.photosRecyclerview.adapter = PhotoListAdapter(displayPhoto)
        binding.swipeRefreshLayout.setOnRefreshListener {
            if(photoListViewModel.isUserSearching){
                photoListViewModel.refresh(photoListViewModel.query.value?:"")
            }
            else{
                photoListViewModel.refresh("")
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }
        navController = findNavController()
        return binding.root
    }





    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_photo_list, menu)
        setUpSearchViewListener(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_clear -> {
                photoListViewModel.apply {
                    changeQueryValue("")
                }
                true
            }
            else ->NavigationUI.onNavDestinationSelected(item,navController) || super.onOptionsItemSelected(item)

        }
    }

    private fun setUpSearchViewListener(menu: Menu) {
        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView
        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    photoListViewModel.apply {
                        changeQueryValue(query ?: "")
                    }
                    onActionViewCollapsed()
                    searchItem.collapseActionView()
                    hideSoftKeyboard()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
            setOnSearchClickListener {
                searchView.setQuery(photoListViewModel.query.value ?: "", false)
            }


        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callbacks?.onPhotoListFragmentDisplayed()
    }

    fun hideSoftKeyboard() {
        view?.let {
            val imm = context?.getSystemService<InputMethodManager>()

            imm?.hideSoftInputFromWindow(
                it.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }


}

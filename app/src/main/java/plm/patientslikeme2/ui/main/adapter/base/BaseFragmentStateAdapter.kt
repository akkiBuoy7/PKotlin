package plm.patientslikeme2.ui.main.adapter.base

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import plm.patientslikeme2.ui.base.BaseFragment

class BaseFragmentStateAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val fragments: ArrayList<Pair<String, BaseFragment<out ViewDataBinding>>>
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position].second
    }
}
package plm.patientslikeme2.utils.extensions

import plm.patientslikeme2.data.model.community.groups.members.NewMembers
import plm.patientslikeme2.data.model.community.groups.mygroups.MyGroup
import plm.patientslikeme2.data.model.community.groups.suggestedgroups.SuggestedGroup
import plm.patientslikeme2.data.model.community.members.Users

fun SuggestedGroup.getMyGroup() : MyGroup {
    val group = MyGroup()
    group.description = this.description
    group.id = this.id
    group.key = this.key
    group.name = this.name
    group.newStats.members = this.stats.members

    return group
}

fun MyGroup.getSuggestedGroup() :  SuggestedGroup{
    val group = SuggestedGroup()
    group.description = this.description
    group.id = this.id
    group.key = this.key
    group.name = this.name
    group.stats.members = this.newStats.members

    return group
}

fun NewMembers.getUser() : Users{
    val user = Users()
    user.name = this.name
    user.id = this.id
    user.login= this.login
    user.profilePic = this.profilePic
    user.address = this.address
    user.briefBio = this.briefBio
    user.sex = this.sex
    user.age = this.age
    return  user
}
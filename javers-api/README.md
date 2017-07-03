### Query data history views

1. Find Change
/javers/changes
 
2. Find Shadow
/javers/shadows 
 
3. Find Snapshot 
/javers/snapshots

### Query types

1. Querying for Entity changes by Instance Id
/javers/{dataType}/entities?instanceId=bob&className=className=org.javers.organization.structure.domain.Person

2. Querying for ValueObjects changed by Value Object
/javers/{dataType}/valueobjects?ownerEntityClass=org.javers.organization.structure.domain.PrimaryAddress&path=primaryAddress

3. Querying for ValueObjects  changed by Value Object Id
/javers/{dataType}/valueobjects?ownerLocalId=bob&ownerEntityClass=className=org.javers.organization.structure.domain.PrimaryAddress&path=primaryAddress

4. Querying for any object changes by Class
/javers/{dataType}?requiredClass=org.javers.organization.structure.domain.Person&requiredClass=org.javers.organization.structure.domain.Document

5. Query for any domain object changes
/javers/{dataType}

### Filters

1. Changed property filter
/javers/{dataType}?propertyName=salary

2. Limit filter
/javers/{dataType}?limit=2

3. Skip filter
/javers/{dataType}?skip=2

4. Author filter
/javers/{dataType}?author=Pam

5. CommitProperty filter
/javers/{dataType}?commitProperty=tenant;ACME&commitProperty=event;promotion

6. CommitDate filter
/javers/{dataType}?from=1467399915&to=1498935915

7. CommitId filter
/javers/{dataType}?commitId=4

8. Snapshot version filter
/javers/{dataType}?version=4

9. Snapshot version filter
/javers/{dataType}?childValueObjects

10. NewObject changes filter
/javers/{dataType}?newObjectChanges